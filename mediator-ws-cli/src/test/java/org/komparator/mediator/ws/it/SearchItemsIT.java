package org.komparator.mediator.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.komparator.mediator.ws.*;
import org.komparator.supplier.ws.BadProductId_Exception;
import org.komparator.supplier.ws.BadProduct_Exception;
import org.komparator.supplier.ws.ProductView;

/**
 * Test suite
 */
public class SearchItemsIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
		
	}

	@AfterClass
	public static void oneTimeTearDown() {
		mediatorClient.clear();
		client1.clear();
		client2.clear();

	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() throws BadProductId_Exception, BadProduct_Exception {

			
			// fill-in test products
			{
				ProductView product = new ProductView();
				product.setId("X1");
				product.setDesc("Basketball");
				product.setPrice(10);
				product.setQuantity(10);
				client1.createProduct(product);
			}
			{
				ProductView product = new ProductView();
				product.setId("Y2");
				product.setDesc("Baseball");
				product.setPrice(20);
				product.setQuantity(20);
				client1.createProduct(product);
			}
			{
				ProductView product = new ProductView();
				product.setId("Z3");
				product.setDesc("Soccer ball");
				product.setPrice(30);
				product.setQuantity(30);
				client1.createProduct(product);
			}
			{
				ProductView product = new ProductView();
				product.setId("T4");
				product.setDesc("Tennis racket");
				product.setPrice(15);
				product.setQuantity(15);
				client2.createProduct(product);
			}
			
		}

	@After
	public void tearDown() {
		mediatorClient.clear();
		client1.clear();
		client2.clear();

	}

	// tests
	// assertEquals(expected, actual);

	// public List<ItemView> searchItems(String descText) throws 
	// InvalidText_Exception

	// bad input tests

	@Test(expected = InvalidText_Exception.class)
	public void searchItemsNullTest() throws InvalidText_Exception {
		mediatorClient.searchItems(null);
	}

	@Test(expected = InvalidText_Exception.class)
	public void searchItemsEmptyTest() throws InvalidText_Exception {
		mediatorClient.searchItems("");
	}

	@Test(expected = InvalidText_Exception.class)
	public void searchItemsWhitespaceTest() throws InvalidText_Exception {
		mediatorClient.searchItems(" ");
	}

	@Test(expected = InvalidText_Exception.class)
	public void searchItemsTabTest() throws InvalidText_Exception {
		mediatorClient.searchItems("\t");
	}

	@Test(expected = InvalidText_Exception.class)
	public void searchItemsNewlineTest() throws InvalidText_Exception {
		mediatorClient.searchItems("\n");
	}
	

	// main tests

	@Test
	public void searchItemsExistsTest() throws InvalidText_Exception {
		List<ItemView>  items = mediatorClient.searchItems("Basketball");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("X1");
		idItem.setSupplierId("A50_Supplier1");
		
		
		assertEquals(10, item.getPrice());
		assertEquals("Basketball", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	}
	
	@Test
	public void searchSingleItemExistsTest() throws InvalidText_Exception, BadProductId_Exception, BadProduct_Exception {
		client1.clear();
		ProductView product = new ProductView();
		product.setId("X1");
		product.setDesc("Basketball");
		product.setPrice(10);
		product.setQuantity(10);
		client1.createProduct(product);
		
		List<ItemView>  items = mediatorClient.searchItems("Basketball");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("X1");
		idItem.setSupplierId("A50_Supplier1");
		
		
		assertEquals(10, item.getPrice());
		assertEquals("Basketball", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	}
	
	@Test
	public void searchItemsAnotherExistsTest() throws InvalidText_Exception {
		List<ItemView>  items = mediatorClient.searchItems("Baseball");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("Y2");
		idItem.setSupplierId("A50_Supplier1");
		
		assertEquals(20, item.getPrice());
		assertEquals("Baseball", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	}
	
	@Test
	public void searchItemsYetAnotherExistsTest() throws InvalidText_Exception {
		List<ItemView>  items = mediatorClient.searchItems("Soccer ball");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("Z3");
		idItem.setSupplierId("A50_Supplier1");
		
		assertEquals(30, item.getPrice());
		assertEquals("Soccer ball", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	}
	
	@Test
	public void searchItemsAnotherServerTest() throws InvalidText_Exception {
		List<ItemView>  items = mediatorClient.searchItems("Tennis racket");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("T4");
		idItem.setSupplierId("A50_Supplier2");
		
		assertEquals(15, item.getPrice());
		assertEquals("Tennis racket", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	}
	
	
	@Test
	public void searchItemsSubStringTest() throws InvalidText_Exception {
		List<ItemView>  items = mediatorClient.searchItems("ball");
		assertEquals(3, items.size());
		ItemView item = items.get(0);
		assertEquals("Basketball", item.getDesc());
		item = items.get(1);
		assertEquals("Baseball", item.getDesc());
		item = items.get(2);
		assertEquals("Soccer ball", item.getDesc());
		
	} 
	
	
	@Test
	public void searchItemsSortDescriptionTest() throws InvalidText_Exception, BadProductId_Exception , BadProduct_Exception {
		
		ProductView product = new ProductView();
		product.setId("Z5");
		product.setDesc("Soccer balls");
		product.setPrice(40);
		product.setQuantity(40);
		client1.createProduct(product);
		
		product = new ProductView();
		product.setId("Z4");
		product.setDesc("Soccer balla");
		product.setPrice(1);
		product.setQuantity(1);
		client1.createProduct(product);
		
	
		List<ItemView>  items = mediatorClient.searchItems("Soccer ball");
		assertEquals(3, items.size());
		ItemView item0 = items.get(0);
		ItemView item1 = items.get(1);
		ItemView item2 = items.get(2);
		
		assertEquals("Soccer ball", item0.getDesc());
		assertEquals("Z3", item0.getItemId().getProductId());
		
		assertEquals("Soccer balla", item1.getDesc());
		assertEquals("Z4", item1.getItemId().getProductId());
		
		assertEquals("Soccer balls", item2.getDesc());
		assertEquals("Z5", item2.getItemId().getProductId());
		
	} 
	
	public void searchItemsSortPriceTest() throws InvalidText_Exception, BadProductId_Exception , BadProduct_Exception {
		
		ProductView product = new ProductView();
		product.setId("Z5");
		product.setDesc("Soccer ball");
		product.setPrice(40);
		product.setQuantity(40);
		client1.createProduct(product);
		
		product = new ProductView();
		product.setId("Z4");
		product.setDesc("Soccer ball");
		product.setPrice(1);
		product.setQuantity(1);
		client1.createProduct(product);
		
	
		List<ItemView>  items = mediatorClient.searchItems("Soccer ball");
		assertEquals(3, items.size());
		ItemView item0 = items.get(0);
		ItemView item1 = items.get(1);
		ItemView item2 = items.get(2);
		
		assertEquals(3, items.size());
		
		assertEquals("Soccer ball", item0.getDesc());
		assertEquals("Z4", item0.getItemId().getProductId());
		assertEquals(1, item0.getPrice());
		
		assertEquals("Soccer balla", item1.getDesc());
		assertEquals("Z3", item0.getItemId().getProductId());
		assertEquals(30, item1.getPrice());
		
		assertEquals("Soccer balls", item2.getDesc());
		assertEquals("Z5", item0.getItemId().getProductId());
		assertEquals(40, item2.getPrice());
		
	} 
	
	
	
	
}
