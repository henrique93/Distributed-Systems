package org.komparator.mediator.ws;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingProvider;

import org.komparator.mediator.ws.cli.MediatorClient;
import org.komparator.mediator.ws.cli.MediatorClientException;
import org.komparator.supplier.ws.BadProductId_Exception;
import org.komparator.supplier.ws.BadQuantity_Exception;
import org.komparator.supplier.ws.BadText_Exception;
import org.komparator.supplier.ws.InsufficientQuantity_Exception;
import org.komparator.supplier.ws.ProductView;
import org.komparator.supplier.ws.SupplierPortType;
import org.komparator.supplier.ws.SupplierService;

import pt.ulisboa.tecnico.sdis.ws.cli.CreditCardClient;
import pt.ulisboa.tecnico.sdis.ws.cli.CreditCardClientException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

@WebService(
		endpointInterface = "org.komparator.mediator.ws.MediatorPortType", 
		wsdlLocation = "mediator.1_0.wsdl", 
		name = "MediatorWebService", 
		portName = "MediatorPort", 
		targetNamespace = "http://ws.mediator.komparator.org/", 
		serviceName = "MediatorService"
)
@HandlerChain(file = "mediator-ws_handler-chain.xml")

public class MediatorPortImpl implements MediatorPortType {

	// end point manager
	private MediatorEndpointManager endpointManager;
	
	private final static String supplier = "A50_Supplier";
	private MediatorClient mc2;
	
	/**
	 * Map to store every existent cart
	 * Map key: String cartId
	 * Map value: Cart cart
	 */
	private Map<String, CartView> carts = Collections.synchronizedMap(new HashMap<String, CartView>());
	
	/**
	 * List to store the shopHistory (every shopping result)
	 * Element's type: ShoppingResultView
	 */
	private List<ShoppingResultView> shopHistory = new ArrayList<ShoppingResultView>();

	/**
	 * imAlive date
	 */
	private Date date = new Date();
	
	public Date getDate() {
		return date;
	}
	
	
	/**
	 * Constructor
	 * @param endpointManager
	 */
	public MediatorPortImpl(MediatorEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
		
		
	}
	

	// Main operations -------------------------------------------------------
	
	/**
	 * Returns a list with the price of a product on different suppliers. The list is orderdered 
	 * by price.
	 */
	public synchronized List<ItemView> getItems(String productId) throws InvalidItemId_Exception {
		
		// check product id
		if (productId == null)
			throwInvalidItemId("Product identifier cannot be null!");
			productId = productId.trim();
		if (productId.length() == 0)
			throwInvalidItemId("Product identifier cannot be empty or whitespace!");
		
		List<ItemView> result = new ArrayList<ItemView>();
        
		try {
		UDDINaming uddiNaming = endpointManager.getUddiNaming();
        Collection<UDDIRecord> UDDIList;
            
        UDDIList = uddiNaming.listRecords(supplier + "%");

		if (UDDIList.isEmpty()) { //Case should never happen
			System.out.println("Not found!");
			throwInvalidItemId("The supplier is unavailable or does not exist");
		} 

		SupplierService service = new SupplierService();
		SupplierPortType port = service.getSupplierPort();

		BindingProvider bindingProvider = (BindingProvider) port;;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		
		ProductView pv = new ProductView();
		ItemView item;
		ItemIdView idView;
		
		for (UDDIRecord record : UDDIList) {
            String url = record.getUrl();
            String name = record.getOrgName();
			item = new ItemView();
			idView = new ItemIdView();
			
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
			pv = port.getProduct(productId);
			if (pv == null)
				continue; //if there is no product found
			idView.setProductId(pv.getId());
			idView.setSupplierId(name);
			
			item.setItemId(idView);
			item.setDesc(pv.getDesc());
			item.setPrice(pv.getPrice());
			
			result.add(item);
			
		}
		
		} catch (UDDINamingException e) {
			e.printStackTrace();
			return null;
		} catch (java.lang.IllegalArgumentException e) {return null;
		} catch (BadProductId_Exception e) {throwInvalidItemId(e.getMessage());}
		
		
		java.util.Collections.sort(result , new Comparator<Object>() {
	        public int compare(final Object o1, final Object o2) {
	        	
	        return ComparatorInt(o1, o2);
	        }
	    } );
		 
		
		return result;
	}
	   
	/**
	 * Returns a list with the products that contains a given description. The list is ordered 
	 * by the product identifier first and then by price.
	 */
	
	
	public synchronized List<ItemView> searchItems(String descText) throws InvalidText_Exception {
		
		// check product description
		if (descText == null)
			throwInvalidText("Product description cannot be null!");
			descText = descText.trim();
		if (descText.length() == 0)
			throwInvalidText("Product description cannot be empty or whitespace!");
		
		List<ItemView> result = new ArrayList<ItemView>();
        
		try {
		UDDINaming uddiNaming = endpointManager.getUddiNaming();
		Collection<UDDIRecord> UDDIList;
		
		UDDIList = uddiNaming.listRecords(supplier + "%");

		if (UDDIList.isEmpty()) { //Case should never happen
			System.out.println("Not found!");
			return null;
		} 

		SupplierService service = new SupplierService();
		SupplierPortType port = service.getSupplierPort();

		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		
		List<ProductView> pv = new ArrayList<ProductView>();
		ItemView item;
		ItemIdView idView;
		
        for (UDDIRecord record : UDDIList) {
            String url = record.getUrl();
            String name = record.getOrgName();

			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
			pv = port.searchProducts(descText);
			
			
			if(pv.isEmpty()) 
				continue;
			
			for (ProductView p : pv) {
				item = new ItemView();
				idView = new ItemIdView();
				idView.setProductId(p.getId());
				idView.setSupplierId(name);
				item.setDesc(p.getDesc());
				item.setPrice(p.getPrice());
				item.setItemId(idView);
				result.add(item);
			}
			
			
			
		}
		
		} catch (UDDINamingException e) {
			e.printStackTrace();
			return null;
		} catch (java.lang.IllegalArgumentException e) {return null;
		} catch (BadText_Exception e) {throwInvalidText(e.getMessage());}
		
		
		java.util.Collections.sort(result , new Comparator<Object>() {
	        public int compare(final Object o1, final Object o2) {
	        	
	        return ComparatorSearch(o1, o2);
	        }
	    } );

		
		return result;
		
		
	}
	
	/**
	 * Places a product into a shopping cart.
	 */
	public synchronized void addToCart(String cartId, ItemIdView itemId, int itemQty) throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		 int hasProduct = 0;
		// check cart identifier
		if (cartId == null || cartId.trim().length() == 0)
			throwInvalidCartId("Cart identifier cannot be null, empty or whitespace");
		// check item quantity
		if (itemQty < 1)
			throwInvalidQuantity("You need to add at least 1 item!");
		if (itemId == null)
			throwInvalidItemId("Item identifier cannot be null");
		String supplierId = itemId.getSupplierId();
		if (supplierId == null || supplierId.trim().length() == 0)
			throwInvalidItemId("Supplier identifier from ItemIdView cannot be null, empty or whitespace");
		String productId = itemId.getProductId();
		if (productId == null || productId.trim().length() == 0)
			throwInvalidItemId("Product identifier from ItemIdView cannot be null, empty or whitespace");
		
		ProductView pv = new ProductView();
		try {
			UDDINaming uddiNaming = endpointManager.getUddiNaming();
			String url = uddiNaming.lookup(supplierId);
            
			if (url == null) {
				throwInvalidItemId("The given supplier is unavailable or does not exist");
			} 

			SupplierService service = new SupplierService();
			SupplierPortType port = service.getSupplierPort();

			BindingProvider bindingProvider = (BindingProvider) port;;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();

			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

			pv = port.getProduct(productId);
				
			if (pv == null) {
				throwInvalidItemId("The given supplier does not have the wanted product");
			}
			
			} catch (UDDINamingException e) {
				e.printStackTrace();
			} catch (java.lang.IllegalArgumentException e) {
				e.printStackTrace();
			} catch (BadProductId_Exception e) {
				e.printStackTrace();
			}
		if (pv.getQuantity() < itemQty) {
			throwNotEnoughItems("This supplier does not have the amount required of this item");
		}
		
		ItemIdView id;
		if (carts.containsKey(cartId)) {
			CartView cart = carts.get(cartId);
			for (CartItemView item : cart.getItems()) {
				id = item.getItem().getItemId();
				boolean productEquals = id.getProductId().equals(itemId.getProductId());
				boolean supplierEquals = id.getSupplierId().equals(itemId.getSupplierId());
				if (productEquals && supplierEquals) {
					int quantity = item.getQuantity() + itemQty;
					if (pv.getQuantity() < quantity) {
						throwNotEnoughItems("This supplier does not have the amount required of this item");
					}
					else {
						item.setQuantity(quantity);
						hasProduct = 1;
						break;
					}
				}
			}
			if (hasProduct == 0) {
				List<ItemView> list =  getItems(itemId.getProductId());
				for (ItemView item : list) {
					id = item.getItemId();
					boolean productEquals = id.getProductId().equals(itemId.getProductId());
					boolean supplierEquals = id.getSupplierId().equals(itemId.getSupplierId());
					if (productEquals && supplierEquals) {
						CartItemView cartItemView = new CartItemView();
						cartItemView.setItem(item);
						cartItemView.setQuantity(itemQty);
						break;
					}
				}
			}
		}
		else {
			List<ItemView> list =  getItems(itemId.getProductId());
			for (ItemView item : list) {
				id = item.getItemId();
				boolean productEquals = id.getProductId().equals(itemId.getProductId());
				boolean supplierEquals = id.getSupplierId().equals(itemId.getSupplierId());
				if (productEquals && supplierEquals) {
					CartItemView cartItemView = new CartItemView();
					cartItemView.setItem(item);
					cartItemView.setQuantity(itemQty);
					CartView cartView = new CartView();
					cartView.setCartId(cartId);
					cartView.getItems().add(cartItemView);
					carts.put(cartId, cartView);
					if (!endpointManager.getWsURL().equals("http://localhost:8072/mediator-ws/endpoint")) {
						try {	
							mc2 = new MediatorClient("http://localhost:8072/mediator-ws/endpoint");	
						} catch (MediatorClientException e) {
							System.out.println("Mediator client is not available");
							e.printStackTrace();
						}
						mc2.updateCart(cartView,cartId);
					}
					break;
				}
			}
		}
		
	 }
    
    
	/**
	 * Purchases all the products that the shopping cart contains.
	 */
	 public synchronized ShoppingResultView buyCart(String cartId, String creditCardNr) throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception {
		ShoppingResultView result = new ShoppingResultView();
		List<CartItemView> purchasedItems = result.getPurchasedItems();
		List<CartItemView> droppedItems = result.getDroppedItems();
		
		//Purchase state when not one item was successfully bought (starter state)
		Result state = Result.fromValue("EMPTY");
		
		result.setResult(state);
		
		CreditCardClient ccClient = null;
		try { 
			ccClient = new CreditCardClient("http://ws.sd.rnl.tecnico.ulisboa.pt:8080/cc");
		} catch (CreditCardClientException e1) {
			e1.printStackTrace();
		} 
		int totalPrice = result.getTotalPrice();
		// check cart identifier
		if (cartId == null || cartId.trim().length() == 0)
			throwInvalidCartId("Cart identifier cannot be null, empty or whitespace");
		if (!carts.containsKey(cartId))
			throwInvalidCartId("Cart does not exist");
		// check credit card number
		if (!ccClient.validateNumber(creditCardNr)) { 
			throwInvalidCreditCard("The given credit card number is not valid");
		}
		
		CartView cart = carts.get(cartId);
		List<CartItemView> itemList = cart.getItems();
		// check if cart is empty
		if (itemList.size() == 0) {
			throwEmptyCart("Cart is empty");
		}
		
		SupplierPortType port = null;
		ProductView pv = new ProductView();
		ItemView itemView;
		ItemIdView itemId;
		int productQuantity = 0;
		int purchaseQuantity = 0;
		for (CartItemView item : itemList) {
			itemView = item.getItem();
			itemId = itemView.getItemId();
			
			try {
				UDDINaming uddiNaming = endpointManager.getUddiNaming();
                Collection<UDDIRecord> UDDIList;
                
                UDDIList = uddiNaming.listRecords(supplier + "%");

				if (UDDIList.isEmpty()) { //Case should never happen
					System.out.println("Not found!");
				} 

				SupplierService service = new SupplierService();
				port = service.getSupplierPort();

				BindingProvider bindingProvider = (BindingProvider) port;;
				Map<String, Object> requestContext = bindingProvider.getRequestContext();
				
                for (UDDIRecord record : UDDIList) {
                    String url = record.getUrl();
                    String name = record.getOrgName();
                    
					requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
					if (name.equals(itemId.getSupplierId())) {
						pv = port.getProduct(itemId.getProductId());
					}
				}
				
			} catch (UDDINamingException e) {
				e.printStackTrace();
			} catch (java.lang.IllegalArgumentException e) {
				e.printStackTrace();
			} catch (BadProductId_Exception e) {
				e.printStackTrace();
			}
			
			productQuantity = pv.getQuantity();
			purchaseQuantity = item.getQuantity();
			
			// If the wanted amount can't be purchased then the product is not purchased
			if (productQuantity < purchaseQuantity) {
				droppedItems.add(item);
			}
			else {
				purchasedItems.add(item);
				totalPrice += (pv.getPrice() * purchaseQuantity);
				try {
					port.buyProduct(pv.getId(), purchaseQuantity);
				} catch (BadProductId_Exception | BadQuantity_Exception | InsufficientQuantity_Exception e) {
					continue;
				}
			}
			
		}
		//Purchase state when every item was successfully bought
		if (purchasedItems.size() == itemList.size()) {
			state = Result.fromValue("COMPLETE");
			result.setResult(state);
		}
		
		//Purchase state when at least one item was not successfully bought
		else if (droppedItems.size() > 0 && purchasedItems.size() > 0) {
			state = Result.fromValue("PARTIAL");
			result.setResult(state);
		}
		
		String uniqueID = UUID.randomUUID().toString();
		result.setId(uniqueID);
		result.setTotalPrice(totalPrice);
		shopHistory.add(result);
		if (!endpointManager.getWsURL().equals("http://localhost:8072/mediator-ws/endpoint")) {
			try {
				mc2 = new MediatorClient("http://localhost:8072/mediator-ws/endpoint");	
			} catch (MediatorClientException e) {
				System.out.println("Mediator client is not available");
				e.printStackTrace();
			}
			mc2.updateShopHistory(result);
		}
		
		return result;
	 }
    
	// Auxiliary operations --------------------------------------------------
    
	
	/**
	 * Returns a diagnostic message not empty.Includes the ping's result of all the known suppliers
	 * to test the connectivity of the whole system.
	 * @param name
	 * @return
	 */
	 public synchronized String ping(String arg0) {

			//System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = endpointManager.getUddiNaming();

			System.out.printf("Looking for '%s'%n", arg0);
			Collection<UDDIRecord> UDDIList;
			try {
				UDDIList = uddiNaming.listRecords(arg0 + "%");
				
				if (UDDIList.isEmpty()) { //Case should never happen
					System.out.println("Not found!");
					return "no server found";
				} 
				else {
					System.out.printf("Found %s%n", UDDIList);
				}

			System.out.println("Creating stub ...");
			SupplierService service = new SupplierService();
			SupplierPortType port = service.getSupplierPort();

			System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();;
			String result = new String();
            for (UDDIRecord record : UDDIList) {
                String url = record.getUrl();
                
				requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
				System.out.println("Remote call ...");
				result += port.ping("Mediator") + "\n";
			}
			return result;
			} catch (UDDINamingException e) {
				e.printStackTrace();
				return "no server found";
			} catch (java.lang.IllegalArgumentException e) {return "no server";}
		 
	 }
	
	/**
	 * Deletes the state of the service. Calls the equivalent operation on every supplier in order
	 * to clear the state of the whole system.
	 */
	public synchronized void clear() {
		UDDINaming uddiNaming = endpointManager.getUddiNaming();

        Collection<UDDIRecord> UDDIList;
		try {
			UDDIList = uddiNaming.listRecords(supplier + "%");
			System.out.printf("Looking for '%s'%n", supplier);
			
			if (UDDIList.isEmpty()) { //Case should never happen
				System.out.println("Not found!");
			} 
			else {
				System.out.printf("Found %s%n", UDDIList);
			}
			
			System.out.println("Creating stub ...");
			SupplierService service = new SupplierService();
			SupplierPortType port = service.getSupplierPort();

			System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();;
            for (UDDIRecord record : UDDIList) {
                String url = record.getUrl();

				requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
				System.out.println("Remote call ...");
				port.clear();
			}
		} catch (UDDINamingException e) {
			e.printStackTrace();
			System.out.println("Server not found");
		}
		carts.clear();
	}
	
	/**
	 * Returns a list with the state of every shopping cart. The state of a shopping cart
	 * is the list of all the products it has
	 */
	public synchronized List<CartView> listCarts() {
		List<CartView> states = new ArrayList<CartView>(carts.values());
		return states;
	}

	/**
	 * Returns a list with every purchase done, orderer by time from the most recent
	 * to the oldest.
	 */
	public synchronized List<ShoppingResultView> shopHistory() {
		List<ShoppingResultView> result = shopHistory;
		Collections.reverse(result);
		return result;
	}
	
	/**
	 * 
	 */
    public void imAlive() {
    	//PRIMARY SERVER
    	if (endpointManager.getWsURL().equals("http://localhost:8071/mediator-ws/endpoint")) {
    		//does nothing
    	}
    	//SECONDARY SERVER
    	else {
    		date = new Date();
    	}
    }
    
	public void updateShopHistory(ShoppingResultView shopResult)  {
		shopHistory.add(shopResult);
	}

	public void updateCart(CartView cart, String cartId) {
		carts.put(cartId, cart);	
	}

	
	// Exception helpers -----------------------------------------------------

	/** Helper method to throw new throwInvalidId exception */
	private void throwInvalidItemId(final String message) throws InvalidItemId_Exception {
		InvalidItemId faultInfo = new InvalidItemId();
		faultInfo.message = message;
		throw new InvalidItemId_Exception(message, faultInfo);
	}
	
	/** Helper method to throw new throwInvalidText exception */
	private void throwInvalidText(final String message) throws InvalidText_Exception {
		InvalidText faultInfo = new InvalidText();
		faultInfo.message = message;
		throw new InvalidText_Exception(message, faultInfo);
	}
	
	/** Helper method to throw new throwInvalidCartId exception */
	private void throwInvalidCartId(final String message) throws InvalidCartId_Exception {
		InvalidCartId faultInfo = new InvalidCartId();
		faultInfo.message = message;
		throw new InvalidCartId_Exception(message, faultInfo);
	}
	
	/** Helper method to throw new throwInvalidQuantity exception */
	private void throwInvalidQuantity(final String message) throws InvalidQuantity_Exception {
		InvalidQuantity faultInfo = new InvalidQuantity();
		faultInfo.message = message;
		throw new InvalidQuantity_Exception(message, faultInfo);
	}
	
	/** Helper method to throw new throwNotEnoughItems exception */
	private void throwNotEnoughItems(final String message) throws NotEnoughItems_Exception {
		NotEnoughItems faultInfo = new NotEnoughItems();
		faultInfo.message = message;
		throw new NotEnoughItems_Exception(message, faultInfo);
	}
	
	/** Helper method to throw new throwInvalidCreditCard exception */
	private void throwInvalidCreditCard(final String message) throws InvalidCreditCard_Exception {
		InvalidCreditCard faultInfo = new InvalidCreditCard();
		faultInfo.message = message;
		throw new InvalidCreditCard_Exception(message, faultInfo);
	}
	
	/** Helper method to throw new throwEmptyCart exception */
	private void throwEmptyCart(final String message) throws EmptyCart_Exception {
		EmptyCart faultInfo = new EmptyCart();
		faultInfo.message = message;
		throw new EmptyCart_Exception(message, faultInfo);
	}
	
	// Other auxiliary functions ------------------------------------------------------------------
	
	//To compare Prices
	public int ComparatorInt(Object o1, Object o2) {
		ItemView it1 = (ItemView) o1;
		ItemView it2 = (ItemView) o2;
	    Integer i1 = new Integer(it1.getPrice());
	    Integer i2 = new Integer(it2.getPrice());
	        return i1.compareTo(i2);
	    }
	
	//To compare ID's
	public int ComparatorId(Object o1, Object o2) {
		ItemView it1 = (ItemView) o1;
		ItemView it2 = (ItemView) o2;
	    String s1 = it1.getItemId().getProductId();
	    String s2 = it2.getItemId().getProductId();
	        return s1.compareTo(s2);
	    }
	
	public int ComparatorSearch(Object o1, Object o2) {
		int result = ComparatorId(o1,o2);
		if(result != 0)
			return result;
		else
			return ComparatorInt(o1,o2);
		
	    }
	
}
