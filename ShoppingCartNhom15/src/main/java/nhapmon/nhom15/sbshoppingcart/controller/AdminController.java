package nhapmon.nhom15.sbshoppingcart.controller;
 
import java.util.List;
 
import org.apache.commons.lang.exception.ExceptionUtils;

import nhapmon.nhom15.sbshoppingcart.dao.AccountDAO;
import nhapmon.nhom15.sbshoppingcart.dao.OrderDAO;
import nhapmon.nhom15.sbshoppingcart.dao.ProductDAO;
import nhapmon.nhom15.sbshoppingcart.entity.Account;
import nhapmon.nhom15.sbshoppingcart.entity.Product;
import nhapmon.nhom15.sbshoppingcart.form.AccountForm;
import nhapmon.nhom15.sbshoppingcart.form.ProductForm;
import nhapmon.nhom15.sbshoppingcart.model.AccountInfo;
import nhapmon.nhom15.sbshoppingcart.model.OrderDetailInfo;
import nhapmon.nhom15.sbshoppingcart.model.OrderInfo;
import nhapmon.nhom15.sbshoppingcart.model.ProductInfo;
import nhapmon.nhom15.sbshoppingcart.pagination.PaginationResult;
import nhapmon.nhom15.sbshoppingcart.validator.ProductFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@Transactional
public class AdminController {
 
    @Autowired
    private OrderDAO orderDAO;
 
    @Autowired
    private ProductDAO productDAO;
 
    @Autowired
    private AccountDAO accountDao;
    
    @Autowired
    private ProductFormValidator productFormValidator;
 
    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);
 
        if (target.getClass() == ProductForm.class) {
            dataBinder.setValidator(productFormValidator); 
        }
    }
 
    // GET: Hiển thị trang login
    @RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
    public String login(Model model) {
 
        return "login";
    }
 
    @RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.GET)
    public String accountInfo(Model model) {
 
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getPassword());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.isEnabled());
 
        model.addAttribute("userDetails", userDetails);
        return "accountInfo";
    }
    @RequestMapping("/Manager")
    public String HomeAdmin() {
    	return "_indexAdmin";
    }
    @RequestMapping("/Chart")
    public String ChartAdmin() {
    	return "_chartAdmin";
    }
    @RequestMapping("/ManageProduct")
    public String listProductAdmin(Model model, //
            @RequestParam(value = "name", defaultValue = "") String likeName,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 6;
        final int maxNavigationPage = 10;
 
        PaginationResult<ProductInfo> result = productDAO.queryProducts(page, //
                maxResult, maxNavigationPage, likeName);
 
        model.addAttribute("paginationProducts", result);
        return "_manageProduct";
    }
    
    @RequestMapping("/ManageAccount")
    public String listAccountAdmin(Model model,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 6;
        final int maxNavigationPage = 10;
 
        PaginationResult<AccountInfo> result = accountDao.queryAccount(page, maxResult, maxNavigationPage);
 
        model.addAttribute("paginationAccount", result);
        return "_manageAccount";
    }
    
    @RequestMapping(value = { "/admin/orderList" }, method = RequestMethod.GET)
    public String orderList(Model model, //
            @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        final int MAX_RESULT = 5;
        final int MAX_NAVIGATION_PAGE = 10;
 
        PaginationResult<OrderInfo> paginationResult //
                = orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);
 
        model.addAttribute("paginationResult", paginationResult);
        return "_manageOrder";
    }
 
    @RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/admin/orderList";
        }
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);
 
        model.addAttribute("orderInfo", orderInfo);
 
        return "_order";
    }
 
    //POST : Remove Single Product
    
    //GET : View Single Product
	@RequestMapping(value = {"/single"}, method = RequestMethod.GET )
	public String singleView(Model model, @RequestParam("productId") String productId) {
		ProductInfo productInfo = this.productDAO.findProductInfo(productId);
		model.addAttribute("productInfo", productInfo );
		return "single";
	}

	// GET: Hiển thị product
	@RequestMapping(value = { "/admin/product" }, method = RequestMethod.GET)
	public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
	    ProductForm productForm = null;
	
	    if (code != null && code.length() > 0) {
	        Product product = productDAO.findProduct(code);
	        if (product != null) {
	            productForm = new ProductForm(product);
	        }
	    }
	    if (productForm == null) {
	        productForm = new ProductForm();
	        productForm.setNewProduct(true);
	    }
	    model.addAttribute("productForm", productForm);
	    return "_addProduct";
	}

	// POST: Save product
	@RequestMapping(value = { "/admin/product" }, method = RequestMethod.POST)
	public String productSave(Model model, //
	        @ModelAttribute("productForm") @Validated ProductForm productForm, //
	        BindingResult result, //
	        final RedirectAttributes redirectAttributes) {
	
	    if (result.hasErrors()) {
	        return "_addProduct";
	    }
	    try {
	        productDAO.save(productForm);
	    } catch (Exception e) {
	        Throwable rootCause = ExceptionUtils.getRootCause(e);
	        String message = rootCause.getMessage();
	        model.addAttribute("errorMessage", message);
	        // Show product form.
	        return "_addProduct";
	    }
	
	    return "redirect:/productList";
	}

	@RequestMapping(value = { "/admin/account" }, method = RequestMethod.GET)
	    public String account(Model model, @RequestParam(value = "userName", defaultValue = "") String userName) {
		    AccountForm accountForm = null;
		
		    if (userName != null && userName.length() > 0) {
	//	        Product product = productDAO.findProduct(code);
		    	Account account = accountDao.findAccount(userName);
		        if (account != null) {
	//	            productForm = new ProductForm(product);
		        	accountForm = new AccountForm(account);
		        }
		    }
		    if (accountForm == null) {
		        accountForm= new AccountForm();
		        accountForm.setNewAccount(true);
		    }
		    model.addAttribute("accountForm", accountForm);
		    return "_addAccount";
		}

	@RequestMapping(value = { "/admin/account" }, method = RequestMethod.POST)
    public String addAccount(Model model, AccountForm accountForm,BindingResult result, final RedirectAttributes redirectAttributes) {
	 	if (result.hasErrors()) {
			return "_addAccount";
		}
	 	try {
			accountDao.insertAccount(accountForm);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
	        String message = rootCause.getMessage();
	        model.addAttribute("errorMessage", message);
	        // Show product form.
	        return "_addAccount";
		}
	 	return "redirect:/ManagerAccount";
	    
	}
	
	//POST : Remove product
	@RequestMapping(value = { "/admin/remove" }, method = RequestMethod.GET)
    public String removeProduct(Model model,//
    		@RequestParam("code") String code,
	        final RedirectAttributes redirectAttributes) {
	
		productDAO.deleteProduct(code);
	    
	
	    return "redirect:/productList";
		
    }
    
    
    
    
    
    
}