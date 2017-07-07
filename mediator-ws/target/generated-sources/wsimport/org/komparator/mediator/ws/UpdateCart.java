
package org.komparator.mediator.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateCart complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateCart">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cart" type="{http://ws.mediator.komparator.org/}cartView" minOccurs="0"/>
 *         &lt;element name="cartId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateCart", propOrder = {
    "cart",
    "cartId"
})
public class UpdateCart {

    protected CartView cart;
    protected String cartId;

    /**
     * Gets the value of the cart property.
     * 
     * @return
     *     possible object is
     *     {@link CartView }
     *     
     */
    public CartView getCart() {
        return cart;
    }

    /**
     * Sets the value of the cart property.
     * 
     * @param value
     *     allowed object is
     *     {@link CartView }
     *     
     */
    public void setCart(CartView value) {
        this.cart = value;
    }

    /**
     * Gets the value of the cartId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCartId() {
        return cartId;
    }

    /**
     * Sets the value of the cartId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCartId(String value) {
        this.cartId = value;
    }

}
