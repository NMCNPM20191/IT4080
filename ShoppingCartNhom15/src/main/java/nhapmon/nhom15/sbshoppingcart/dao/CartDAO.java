package nhapmon.nhom15.sbshoppingcart.dao;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import nhapmon.nhom15.sbshoppingcart.model.CartInfo;
import nhapmon.nhom15.sbshoppingcart.model.CartLineInfo;

@Transactional
@Repository
public class CartDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	public void emptyCart(CartInfo cartInfo) {
		
		Session session = sessionFactory.getCurrentSession();
		cartInfo.getCartLines().clear();
		
	}
}
