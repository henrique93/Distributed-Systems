package org.komparator.supplier.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.komparator.supplier.ws.*;

/**
 * Test suite
 */
public class BuyProductIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() throws BadProductId_Exception, BadProduct_Exception {
		client.clear();

		// fill-in test products
		// (since getProduct is read-only the initialization below
		// can be done once for all tests in this suite)
		{
			ProductView product = new ProductView();
			product.setId("X1");
			product.setDesc("Basketball");
			product.setPrice(10);
			product.setQuantity(10);
			client.createProduct(product);
		}
		{
			ProductView product = new ProductView();
			product.setId("Y2");
			product.setDesc("Baseball");
			product.setPrice(20);
			product.setQuantity(20);
			client.createProduct(product);
		}
		{
			ProductView product = new ProductView();
			product.setId("Z3");
			product.setDesc("Soccer ball");
			product.setPrice(30);
			product.setQuantity(30);
			client.createProduct(product);
		}
	}

	@After
	public void tearDown() {
		client.clear();
	}

	// tests
	// assertEquals(expected, actual);

	// public String buyProduct(String productId, int quantity)
	// throws BadProductId_Exception, BadQuantity_Exception,
	// InsufficientQuantity_Exception {
	// bad input tests

	@Test(expected = BadProductId_Exception.class)
	public void buyProductNullIdTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception  {
		client.buyProduct(null, 3);
	}

	@Test(expected = BadProductId_Exception.class)
	public void buyProductEmptyIdTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception  {
		client.buyProduct("", 3);
	}

	@Test(expected = BadProductId_Exception.class)
	public void buyProductWhitespaceIdTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct(" ", 3);
	}

	@Test(expected = BadProductId_Exception.class)
	public void buyProductTabIdTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("\t", 3);
	}

	@Test(expected = BadProductId_Exception.class)
	public void buyProductNewlineIdTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("\n", 3);
	}
	
	@Test(expected = BadQuantity_Exception.class)
	public void buyProductNegativeQuantityTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("X1", -1);
	}
	
	@Test(expected = BadQuantity_Exception.class)
	public void buyProductZeroQuantityTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("X1", 0);
	}

	// main tests
	
	@Test
	public void buyProductTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		client.buyProduct("X1", 5);
		ProductView product = client.getProduct("X1");
		assertEquals(5, product.getQuantity());
		
	}
	
	@Test(expected = BadProductId_Exception.class)
	public void buyProducttNotExistsTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		// when product does not exist, BadProductId_Exception should be returned
		client.buyProduct("P4", 1);
	}

	@Test(expected = BadProductId_Exception.class)
	public void buyProductLowercaseNotExistsTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {
		// product identifiers are case sensitive,
		// so "z1" is not the same as "Z1"
		client.buyProduct("z3", 1);
	}
	
	@Test(expected = InsufficientQuantity_Exception.class)
	public void buyProductInsuficientQuantityTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {	
		// when Quantity is too high, InsufficientQuantity_Exception should be returned
		client.buyProduct("X1", 88);
	}
	
	@Test
	public void buyProductExactQuantityTest() throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception {	
		// when Quantity is exactly the quantity of the product
		client.buyProduct("Y2", 20);
		ProductView product = client.getProduct("Y2");
		assertEquals(0, product.getQuantity());
		
	}
	
}
