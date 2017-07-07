
package org.komparator.supplier.ws;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SupplierPortType", targetNamespace = "http://ws.supplier.komparator.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SupplierPortType {


    /**
     * 
     */
    @WebMethod
    @RequestWrapper(localName = "clear", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.Clear")
    @ResponseWrapper(localName = "clearResponse", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.ClearResponse")
    @Action(input = "http://ws.supplier.komparator.org/Supplier/clearRequest", output = "http://ws.supplier.komparator.org/Supplier/clearResponse")
    public void clear();

    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "ping", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.PingResponse")
    @Action(input = "http://ws.supplier.komparator.org/Supplier/pingRequest", output = "http://ws.supplier.komparator.org/Supplier/pingResponse")
    public String ping(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param quantity
     * @param productId
     * @return
     *     returns java.lang.String
     * @throws InsufficientQuantity_Exception
     * @throws BadQuantity_Exception
     * @throws BadProductId_Exception
     */
    @WebMethod
    @WebResult(name = "purchaseId", targetNamespace = "")
    @RequestWrapper(localName = "buyProduct", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.BuyProduct")
    @ResponseWrapper(localName = "buyProductResponse", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.BuyProductResponse")
    @Action(input = "http://ws.supplier.komparator.org/Supplier/buyProductRequest", output = "http://ws.supplier.komparator.org/Supplier/buyProductResponse", fault = {
        @FaultAction(className = BadProductId_Exception.class, value = "http://ws.supplier.komparator.org/Supplier/buyProduct/Fault/BadProductId"),
        @FaultAction(className = BadQuantity_Exception.class, value = "http://ws.supplier.komparator.org/Supplier/buyProduct/Fault/BadQuantity"),
        @FaultAction(className = InsufficientQuantity_Exception.class, value = "http://ws.supplier.komparator.org/Supplier/buyProduct/Fault/InsufficientQuantity")
    })
    public String buyProduct(
        @WebParam(name = "productId", targetNamespace = "")
        String productId,
        @WebParam(name = "quantity", targetNamespace = "")
        int quantity)
        throws BadProductId_Exception, BadQuantity_Exception, InsufficientQuantity_Exception
    ;

    /**
     * 
     * @return
     *     returns java.util.List<org.komparator.supplier.ws.PurchaseView>
     */
    @WebMethod
    @WebResult(name = "purchases", targetNamespace = "")
    @RequestWrapper(localName = "listPurchases", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.ListPurchases")
    @ResponseWrapper(localName = "listPurchasesResponse", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.ListPurchasesResponse")
    @Action(input = "http://ws.supplier.komparator.org/Supplier/listPurchasesRequest", output = "http://ws.supplier.komparator.org/Supplier/listPurchasesResponse")
    public List<PurchaseView> listPurchases();

    /**
     * 
     * @param productId
     * @return
     *     returns org.komparator.supplier.ws.ProductView
     * @throws BadProductId_Exception
     */
    @WebMethod
    @WebResult(name = "product", targetNamespace = "")
    @RequestWrapper(localName = "getProduct", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.GetProduct")
    @ResponseWrapper(localName = "getProductResponse", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.GetProductResponse")
    @Action(input = "http://ws.supplier.komparator.org/Supplier/getProductRequest", output = "http://ws.supplier.komparator.org/Supplier/getProductResponse", fault = {
        @FaultAction(className = BadProductId_Exception.class, value = "http://ws.supplier.komparator.org/Supplier/getProduct/Fault/BadProductId")
    })
    public ProductView getProduct(
        @WebParam(name = "productId", targetNamespace = "")
        String productId)
        throws BadProductId_Exception
    ;

    /**
     * 
     * @param descText
     * @return
     *     returns java.util.List<org.komparator.supplier.ws.ProductView>
     * @throws BadText_Exception
     */
    @WebMethod
    @WebResult(name = "products", targetNamespace = "")
    @RequestWrapper(localName = "searchProducts", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.SearchProducts")
    @ResponseWrapper(localName = "searchProductsResponse", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.SearchProductsResponse")
    @Action(input = "http://ws.supplier.komparator.org/Supplier/searchProductsRequest", output = "http://ws.supplier.komparator.org/Supplier/searchProductsResponse", fault = {
        @FaultAction(className = BadText_Exception.class, value = "http://ws.supplier.komparator.org/Supplier/searchProducts/Fault/BadText")
    })
    public List<ProductView> searchProducts(
        @WebParam(name = "descText", targetNamespace = "")
        String descText)
        throws BadText_Exception
    ;

    /**
     * 
     * @param productToCreate
     * @throws BadProduct_Exception
     * @throws BadProductId_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "createProduct", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.CreateProduct")
    @ResponseWrapper(localName = "createProductResponse", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.CreateProductResponse")
    @Action(input = "http://ws.supplier.komparator.org/Supplier/createProductRequest", output = "http://ws.supplier.komparator.org/Supplier/createProductResponse", fault = {
        @FaultAction(className = BadProductId_Exception.class, value = "http://ws.supplier.komparator.org/Supplier/createProduct/Fault/BadProductId"),
        @FaultAction(className = BadProduct_Exception.class, value = "http://ws.supplier.komparator.org/Supplier/createProduct/Fault/BadProduct")
    })
    public void createProduct(
        @WebParam(name = "productToCreate", targetNamespace = "")
        ProductView productToCreate)
        throws BadProductId_Exception, BadProduct_Exception
    ;

    /**
     * 
     * @return
     *     returns java.util.List<org.komparator.supplier.ws.ProductView>
     */
    @WebMethod
    @WebResult(name = "products", targetNamespace = "")
    @RequestWrapper(localName = "listProducts", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.ListProducts")
    @ResponseWrapper(localName = "listProductsResponse", targetNamespace = "http://ws.supplier.komparator.org/", className = "org.komparator.supplier.ws.ListProductsResponse")
    @Action(input = "http://ws.supplier.komparator.org/Supplier/listProductsRequest", output = "http://ws.supplier.komparator.org/Supplier/listProductsResponse")
    public List<ProductView> listProducts();

}
