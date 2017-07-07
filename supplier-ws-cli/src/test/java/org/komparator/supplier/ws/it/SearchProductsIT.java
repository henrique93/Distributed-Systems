package org.komparator.supplier.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.komparator.supplier.ws.*;

/**
 * Test suite
 */
public class SearchProductsIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws BadProductId_Exception, BadProduct_Exception {
		// clear remote service state before all tests
		client.clear();
		
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
	@AfterClass
	public static void oneTimeTearDown() {
		client.clear();
	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	// tests
	// assertEquals(expected, actual);

	// public List<ProductView> searchProducts(String descText) throws
	// BadText_Exception

	// bad input tests

		@Test(expected = BadText_Exception.class)
		public void searchProductsNullTest() throws BadText_Exception {
			client.searchProducts(null);
		}

		@Test(expected = BadText_Exception.class)
		public void searchProductsEmptyTest() throws BadText_Exception {
			client.searchProducts("");
		}

		@Test(expected = BadText_Exception.class)
		public void searchProductsWhitespaceTest() throws BadText_Exception {
			client.searchProducts(" ");
		}

		@Test(expected = BadText_Exception.class)
		public void searchProductsTabTest() throws BadText_Exception {
			client.searchProducts("\t");
		}

		@Test(expected = BadText_Exception.class)
		public void searchProductsNewlineTest() throws BadText_Exception {
			client.searchProducts("\n");
		}

	
	// main tests

		@Test
		public void searchProductsExistsTest() throws BadText_Exception {
			List<ProductView> products = client.searchProducts("Basketball");
			ProductView product = products.get(0);
			assertEquals("X1", product.getId());
			assertEquals(10, product.getPrice());
			assertEquals(10, product.getQuantity());
			assertEquals("Basketball", product.getDesc());
		}

		@Test
		public void searchProductsAnotherExistsTest() throws BadText_Exception {
			List<ProductView> products = client.searchProducts("Baseball");
			ProductView product = products.get(0);
			assertEquals("Y2", product.getId());
			assertEquals(20, product.getPrice());
			assertEquals(20, product.getQuantity());
			assertEquals("Baseball", product.getDesc());
		}

		@Test
		public void searchProductsYetAnotherExistsTest() throws BadText_Exception {
			List<ProductView> products = client.searchProducts("Soccer ball");
			ProductView product = products.get(0);
			assertEquals("Z3", product.getId());
			assertEquals(30, product.getPrice());
			assertEquals(30, product.getQuantity());
			assertEquals("Soccer ball", product.getDesc());
		}

		
	
		@Test
		public void searchProductsNotExistsTest() throws BadText_Exception {
			// when product does not exist, null should be returned
			List<ProductView> products = client.searchProducts("Handegg");
			assertTrue(products.isEmpty());
		}

		
		@Test
		public void searchProductsLowercaseNotExistsTest() throws BadText_Exception {
			// product description are case sensitive,
			// so "BASKETBALL" is not the same as "Basketball"
			List<ProductView> products = client.searchProducts("BASKETBALL");
			assertTrue(products.isEmpty());
		}
		
		@Test
		public void searchProductsSubStringTest() throws BadText_Exception {
			//verify the size of search contents and if it contains a word in the description
			//when "ball" is searched, it returns the 3 products that contain "ball" in it
			List<ProductView> products = client.searchProducts("Basket");
			ProductView product = products.get(0);
			assertEquals(1, products.size());
			assertEquals("Basketball", product.getDesc());
			products = client.searchProducts("ball");
			assertEquals(3, products.size());
			product = products.get(0);
			assertEquals("Basketball", product.getDesc());
			product = products.get(1);
			assertEquals("Baseball", product.getDesc());
			product = products.get(2);
			assertEquals("Soccer ball", product.getDesc());
		}
		
}
