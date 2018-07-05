## Controller层统一写法    

1.对控制层定义注解规范性

解释：
在面向数据接口编程中，返回前端一定为数据对象而不包含视图，因此一般会使用@Controller
定义其为视图层，同时定义方法返回为@ResponseBody使得对象被SpringMVC使用Jackson序列化
为json文本。为避免重复性劳动定义因此在Controller视图层定义时直接使用@RestController
定义即可。

````java
//反例代码：

@Controller
public class EtymaController { 

    @ResponseBody 
    public int saveEtymaAction(Etyma etyma) throws IOException {
        // ...
    }
 }

//正例代码：
@RestController
public class EtymaController { 

    public int saveEtymaAction(Etyma etyma) throws IOException {
  	    // ...
    }
 }
````

2.对于DI注入统一规范

解释：
在早期Spring框架中官方任务setter注入方式更为灵活且强大，因此开始之初推荐大家使用setter的
方式进行注入，但是随着Spring发展以及项目进行，实践任务使用构造函数的方式进行注入是更为恰当
的方法。详细信息可以擦考Spring官方：
http://docs.spring.io/spring-framework/docs/current/spring-framework-reference/htmlsingle/#beans-factory-collaborators

```java
//反例代码：

  
public class EtymaController { 
    
    @Autowired
    private IEtymaService etymaService;
 }


//正例代码：

public class EtymaController { 

    @Autowired
    public EtymaController(IEtymaService etymaService) {
        this.etymaService = etymaService;
    }
 }
```

3.Controller的命名空间需要定义规范性

解释：
对于一个Controller而言其下的数据接口理当是统一类型，因此需要指定这一类的数据口的共同命
名空间，其次假如命名空间不进行指定则会导致很大概率性的接口映射名称碰撞问题

```java
//反例代码：
  
public class EtymaController { 
    
    @RequestMapping(value = "/saveEtyma") 
    public int saveEtymaAction(Etyma etyma) throws IOException {
        // ...
    }
 }


//正例代码：

@RequestMapping("etymaManage") 
public class EtymaController { 

    @RequestMapping("saveEtyma") 
    public int saveEtymaAction(Etyma etyma) throws IOException {
        // ...
    }
 }
```

4.对路由进行定义时将其定义为私有全局静态final形规范性

解释：
假象如果此Controller空间下存在10个数据接口，代码行数为300行，此时有一天让你去维护某个
接口方法，我想你能找到此方法的形式只能如下三种，第一，老实的从上至下寻找方法；第二，打开
文件内搜索快捷键进行关键字查找，第三，打开文件方法快捷键进行方法寻找。但是倘若你的接口路
映射是在全局变量定义的，你只需要点击一下即可直接找到数据接口方法。

```java
//反例代码：
  
public class EtymaController { 
    
    @RequestMapping(value = "/saveEtyma") 
    public int saveEtymaAction(Etyma etyma) throws IOException {
        // ...
    }
 }


//正例代码：

@RequestMapping("etymaManage") 
public class EtymaController { 
    
    private static final String SAVE_ETYMA = "saveEtyma";
    
    @RequestMapping(SAVE_ETYMA) 
    public int saveEtymaAction(Etyma etyma) throws IOException {
        // ...
    }
 }
```

5.对于POJO的运用统一规范性

解释：
试想一下假如User对象是数据库表的映射对象那么跟前端直接对接数据时，还用此对象，会不会导致
各种敏感数据或者字段对外暴露呢，因此对于Controller层的数据交换必须严格规范，接受数据要么
多参数封装类型接收要么定义DTO接收（！！！注意不允许基本类型接收！！！），同时对于返回前端
数据对象必须进行定义VO接收

```java
//反例代码：
  
public class EtymaController { 
    
    @RequestMapping(value = "/saveEtyma") 
    public int saveEtymaAction(Etyma etyma) throws IOException {
        // ...
    }
 }


//正例代码：

@RequestMapping("etymaManage") 
public class EtymaController { 
    
    private static final String SAVE_ETYMA = "saveEtyma";
    
    @RequestMapping(SAVE_ETYMA) 
    public int saveEtymaAction(EtymaDto etymaDto) throws IOException {
        // ...
    }
 }
```

6.返回前端对象一致，保证接口的高统一
解释：
想一下假如每一个接口返回给前端的数据形式不一致，其中还得掺杂各种报错，那么导致现象出现如
下两种,第一，前端无法做到对接口的状态等处理，因为各个接口返回形式缤纷。第二就算某个接口的，
在异常返回，正常返回，报错返回不可能一致，那么假设你是前端人员，你调用的接口在各种情况下，有
各种返回，你将有什么感受

```java
//反例代码：

@Controller
public class EtymaController { 
    
    @RequestMapping(value = "/findEtymaList")
    @ResponseBody
    public List<Etyma> findEtymaListAction() {
        return etymaService.findEtymaListService();
    }
 }


//正例代码：

@RestController
@RequestMapping("etymaManage") 
public class EtymaController { 
    
    private static final String FINDETYMALIST = "findEtymaList";
    
    @PostMapping(FINDETYMALIST) 
    public ResponseResult<Object> findEtymaListAction() {
        return ResultResponseUtils.success(etymaService.findEtymaListService());
    }
 }
```

7.接口请求方式需要明确规范
解释：
需要制定请求方式GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE

```java
//反例代码：

@Controller
public class EtymaController { 
    
    @RequestMapping(value = "/findEtymaList")
    @ResponseBody
    public List<Etyma> findEtymaListAction() {
        return etymaService.findEtymaListService();
    }
 }


//正例代码：

@RestController
@RequestMapping("etymaManage") 
public class EtymaController { 
    
    private static final String FINDETYMALIST = "findEtymaList";
    
    @PostMapping(FINDETYMALIST) 
    public ResponseResult<Object> findEtymaListAction() {    
        return ResultResponseUtils.success(etymaService.findEtymaListService());
    }
 }
```

8.接收参数必须根据业务规则条件进行校验
解释：
接受前端数据，假设不校验直接进行操作岂不是有点太直接了

```java
//反例代码：

@Controller
public class EtymaController { 
    
    @RequestMapping(value = "/saveEtyma")
    @ResponseBody
    public int saveEtymaAction(F etyma) throws IOException {
        return etymaService.saveEtymaService(etyma);
    }
 }


//正例代码：

@RestController
@RequestMapping("etymaManage")
public class EtymaController { 
    
    private static final String FINDETYMALIST = "findEtymaList";
    
    @PostMapping(FINDETYMALIST) 
    public ResponseResult<Object> saveEtymaAction(Etyma etyma) throws IOException {
        //if ()
        //...
        //if ()
        return ResultResponseUtils.success(etymaService.saveEtymaService(etyma));
    }
 }
```

## DO(JavaBean)的规范写法

1.使用注解
解释：
对于Getter、Setter、有参、无参、ToString,EqualsAndHashCode在非必要情况加应当使用注解
```java
//反例代码：

// 略

//正例代码：
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Etyma {

    /**
     * 词根ID
     */
    private Integer etymaId;
    /**
     * 英文名称
     */
    private String enFullName;
    /**
     * 英文简写
     */
    private String enShortName;
    /**
     * 中文含义
     */
    private String chName;
    /**
     * 操作时间
     */
    private String creTime;
    /**
     * 操作人角色实例ID
     */
    private Integer creId;
    /**
     * 是否有效 0无效,1有效
     */
    private Integer isEffect;
    /**
     * 备注
     */
    private String remark;
}
```

2.生成序列化UID
解释：
对于一个Bean对其加识别号是应当的
```java
//反例代码：

public class Etyma {
    /**
     * 词根ID
     */
    private Integer etymaId;
}

//正例代码：
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Etyma implements Serializable {
    
    private static final long serialVersionUID = -1904135916207574890L;
    /**
     * 词根ID
     */
    private Integer etymaId;
}
```
## Service层的规范写法

```
jira-iss
commit
commit