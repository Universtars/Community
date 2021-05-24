package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) // ① 指定使用bean对象池
public class CommunityApplicationTests implements ApplicationContextAware { // ② 实现ApplicationContextAware

	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 此时就获取到了所有的bean，即applicationContext
		// 我们使用一下它试试看
		this.applicationContext = applicationContext;
	}

	@Test
	public void contextLoads() {
		// 获得bean的方式一，指定其类名
		// 使用bean管理对象的好处一：
		// 		调用的不是bean本身，而是其接口。面向接口编程的思想。降低了bean之间的耦合度
		// 使用bean管理对象的好处二：
		//		需要更改某处接口的试下类时，在被调用的地方可以少做修改
		//		①：在原来bean的位置，增加注解:@Primary
		AlphaDao dao = applicationContext.getBean(AlphaDao.class);
		System.out.println(dao.sayHello());

		// 获得bean的方式二，指定其名字
		//		②：在调用处，指定其调用的bean
		dao = applicationContext.getBean("AlphaMyBatis",AlphaDao.class);
		System.out.println(dao.sayHello());
	}


	@Test
	public void testBeanManagement(){
		// 被Spring容器管理的bean：默认时单例模式，只有一个对象
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);

		alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}


	@Test
	public void testSimpleDate(){
		SimpleDateFormat simpleDateFormat =
				applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired  // 直接加在属性前，将xxx类型的bean注入到属性
	@Qualifier("AlphaMyBatis")  // （可选）用于指定某个具体的实现类
	private AlphaDao diDao;

	@Test
	public void testDI(){
		AlphaDao myDao = diDao;
		System.out.println(myDao.sayHello());
	}
}
