package cn.keyss.test.unitils;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.unitils.core.TestListener;
import org.unitils.core.Unitils;
import org.unitils.core.junit.*;
import org.unitils.database.DatabaseUnitils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * 集成测试基类
 *
 * 关注H2数据库测试前后比对，帮助开发完成数据支持的集成测试
 *
 */
public class UnitilsRunner extends SpringJUnit4ClassRunner {
	protected Object test;
	protected TestListener unitilsTestListener;
	private static boolean dataBaseInitFlag = false;
	private static String DB_FILE_KEY = "db.init.files";

	public UnitilsRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		this.unitilsTestListener = getUnitilsTestListener();

		if (!dataBaseInitFlag) {
			DataSource ds = DatabaseUnitils.getDataSource();
			String sqlFiles = this.getTestContextManager().getTestContext().getApplicationContext().getEnvironment()
					.getProperty(DB_FILE_KEY, "createdb.sql");
			for (String sqlFile : sqlFiles.split(",")) {
				if (!StringUtils.isEmpty(sqlFile)) {
					Resource resource = new ClassPathResource(sqlFile, ClassUtils.getDefaultClassLoader());
					ResourceDatabasePopulator populator = new ResourceDatabasePopulator(resource);
					DatabasePopulatorUtils.execute(populator, ds);
				}
			}
			dataBaseInitFlag = true;
		}
	}

	@Override
	protected Statement classBlock(RunNotifier notifier) {
		Class<?> testClass = getTestClass().getJavaClass();
		Statement statement = super.classBlock(notifier);
		statement = new BeforeTestClassStatement(testClass, unitilsTestListener, statement);
		return statement;
	}

	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object test) {
		this.test = test;
		Statement statement = super.methodInvoker(method, test);
		statement = new BeforeTestMethodStatement(unitilsTestListener, statement, method.getMethod(), test);
		statement = new AfterTestMethodStatement(unitilsTestListener, statement, method.getMethod(), test);
		return statement;
	}

	@Override
	protected Statement methodBlock(FrameworkMethod method) {
		Method testMethod = method.getMethod();
		Statement statement = super.methodBlock(method);
		statement = new BeforeTestSetUpStatement(test, testMethod, unitilsTestListener, statement);
		statement = new AfterTestTearDownStatement(unitilsTestListener, statement, test, testMethod);
		return statement;
	}

	protected TestListener getUnitilsTestListener() {
		return Unitils.getInstance().getTestListener();
	}
}