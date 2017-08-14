package com.baidu.action;

import com.baidu.domain.Customer;
import com.baidu.utils.MailUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZhangPei on 2017/8/2.
 */
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@Controller
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {
    //模型对象
    private Customer customer = new Customer();

    @Override
    public Customer getModel() {
        return customer;
    }

    //发送短信模块
    @Autowired
    @Qualifier("jmsQueueTemplate")
    //这块需要指明自己需要那种消息对象，这里使用queue
    private JmsTemplate jt;

    @Action(value = "sendMessage")
    public String sendMessage() throws IOException {
        ServletActionContext.getRequest().setAttribute("identifyCode", null);
        String identifyCode = RandomStringUtils.randomNumeric(6);
        System.err.println(identifyCode);
        final String content = "尊敬的用户您好，您本次的验证码为" + identifyCode + "，服务电话 4008-123-123";
        //以下是发短信
//      String result = SmsUtils.sendSmsByHTTP(customer.getTelephone(), content);
        //现在使用MQ进行发短信，bos_fore中进行生成短信内容，传递到短信模块进行发送
        jt.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone",customer.getTelephone());
                mapMessage.setString("msg",content);
                return mapMessage;
            }
        });

        ServletActionContext.getRequest().getSession().setAttribute("identifyCode", identifyCode);
        /*if (result.startsWith("000")) {
            ServletActionContext.getRequest().getSession().setAttribute("identifyCode", identifyCode);
        } else {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("0");
        }*/
        return NONE;
    }

    //注册功能
    private String identifyCode;

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    //是否同意协议
    private String agree;

    public void setAgree(String agree) {
        this.agree = agree;
    }

    //用于存储激活码
    private RedisTemplate<String, String> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Action(value = "regist", results = {@Result(name = "success", type = "redirect", location = "./signup-success.html"), @Result(name = "input", type = "redirect", location = "./signup.html")})
    public String regist() {
        //拿到session中的验证码
        String code = (String) ServletActionContext.getRequest().getSession().getAttribute("identifyCode");
        if (this.identifyCode == null || !this.identifyCode.equals(code) || this.agree == null) {
            return INPUT;
        } else if (this.identifyCode.equals(code)) {
            //调用crm系统进行处理相关客户数据
            String url = "http://localhost:9008/crm_management/services/customerService/saveCustomer";
            WebClient.create(url).type(MediaType.APPLICATION_JSON).post(customer);
            //发送激活邮件
            String activeCode = UUID.randomUUID().toString().replace("-", "");
            //以下绑定是根据电话号进行绑定的。
            redisTemplate.opsForValue().set(customer.getTelephone(), activeCode, 24, TimeUnit.HOURS);
            String content = "您好，你已经注册成功请点击" + "<a href = '" + MailUtils.activeUrl + "?telephone=" + customer.getTelephone() + "&activeCode=" + activeCode + "'>激活按钮</a>进行激活！";
            MailUtils.sendMail("激活邮件", content, customer.getEmail());
            System.out.println(content);
        }
        return SUCCESS;
    }

    //激活邮件操作
    private String activeCode;

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    @Action(value = "activeMail", results = {@Result(name = "success", type = "redirect", location = "./login.html")})
    public String activeMail() throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=utf-8");
        String activeRedisCode = redisTemplate.opsForValue().get(customer.getTelephone());
        System.err.println(activeRedisCode + "==" + activeCode);
        if (activeCode == null || !activeCode.equals(activeRedisCode)) {
            response.getWriter().println("激活失败！");
        } else {
            String url1 = "http://localhost:9008/crm_management/services/customerService/findCustomer/" + customer.getTelephone();
            //使用get的意思是使用get请求，并且返回值是一个对象，使用getcollection时返回的是集合。
            Customer customerResult = WebClient.create(url1).accept(MediaType.APPLICATION_JSON).get(Customer.class);
            if (customerResult.getType() == null || customerResult.getType() != 1) {
                //没有激活过
                customerResult.setType(1);
                String url2 = "http://localhost:9008/crm_management/services/customerService/saveCustomer";
                WebClient.create(url2).type(MediaType.APPLICATION_JSON).post(customerResult);
                response.getWriter().write("激活成功，请前往登录！");
                return SUCCESS;
            } else {
                response.getWriter().print("已经激活，无需重复激活！");
            }
        }
        return NONE;
    }

    //用户登录功能
    private Integer article;    //登录方式

    public void setArticle(Integer article) {
        this.article = article;
    }

    @Action(value = "login", results = {@Result(name = "success", type = "redirect", location = "index.html"), @Result(name = "input", type = "redirect", location = "login.html")})
    public String login() {
        Customer result = null;
        if (article == 0) {     //使用密码登录
            String url = "http://localhost:9008/crm_management/services/customerService/loginByEmail?email=" + customer.getEmail() + "&password=" + customer.getPassword();
            result = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Customer.class);
        } else if (article == 1) {     //使用手机号登录
            String url = "http://localhost:9008/crm_management/services/customerService/loginByTelephone?telephone=" + customer.getTelephone() + "&password=" + customer.getPassword();
            result = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Customer.class);
        }
        if (result == null) {
            return INPUT;
        } else {
            //将用户存入session中
            ServletActionContext.getRequest().getSession().setAttribute("customer", result);
        }
        return SUCCESS;
    }
}
