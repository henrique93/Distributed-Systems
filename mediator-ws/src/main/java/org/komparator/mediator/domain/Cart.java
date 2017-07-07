package org.komparator.mediator.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.komparator.mediator.ws.ItemIdView;
import org.komparator.mediator.ws.ItemView;
import org.komparator.supplier.ws.ProductView;

public class Cart {
	
	private int cartId;
	private Map<ItemIdView, ProductView> products = Collections.synchronizedMap(new HashMap<ItemIdView, ProductView>());
	
	
	/** Getters */
	public int getCartId() {
		return cartId;
	}
	
	public Collection<ProductView> getProducts() {
		return products.values();
	}
	/**			*/
	
	/** Setters */
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	/**			*/
	
	

	public void addProduct(ProductView product, ItemIdView itemId, int quantity) {
		if (products.containsKey(itemId)) {
			
		}
		else {
			products.put(itemId, product);
		}
	}
	
	/**
	 * Removes the ItemView that corresponds to the give ItemIdView
	 * @param itemId	ItemIdView (Key)
	 */
	public void removeItem(ItemIdView itemId) {
		//items.remove(itemId);
	}
	
	/**
	 * Returns the ItemView that corresponds to the given ItemIdView
	 * @param itemId	ItemIdView
	 * @return			Correspondent ItemView
	 */
	public ItemView getItem(ItemIdView itemId) {
		return null;
	}
}
