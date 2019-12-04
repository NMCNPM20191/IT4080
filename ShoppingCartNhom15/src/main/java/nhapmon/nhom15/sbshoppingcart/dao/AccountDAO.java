package nhapmon.nhom15.sbshoppingcart.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import nhapmon.nhom15.sbshoppingcart.entity.Account;
import nhapmon.nhom15.sbshoppingcart.form.AccountForm;
import nhapmon.nhom15.sbshoppingcart.model.AccountInfo;
import nhapmon.nhom15.sbshoppingcart.pagination.PaginationResult;

@Transactional
@Repository
public class AccountDAO {

	@Autowired
	private SessionFactory sessionFactory;
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public Account findAccount(String userName) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Account.class, userName);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void insertAccount(AccountForm accountForm) {
		Session session = this.sessionFactory.getCurrentSession();
		String user_name = accountForm.getUserName();
		
		Account account = null;
		
		boolean isNew = false;
		if(user_name!=null) {
			account = this.findAccount(user_name);
		}
		if (account==null) {
			isNew = true;
			account = new Account();	
		}
		account.setUserName(user_name);
		account.setActive(accountForm.isActive());
//		account.setEncrytedPassword(accountForm.getEncrytedPassword());
		String encodedPassword = passwordEncoder.encode(accountForm.getEncrytedPassword());
		account.setEncrytedPassword(encodedPassword);
		account.setUserRole(accountForm.getUserRole());
		if (isNew) {
			session.persist(account);
		}
		session.flush();
	};

	public PaginationResult<AccountInfo> queryAccount(int page, int maxResult, int maxNavigationPage ) {
		String sql = "Select new  " + AccountInfo.class.getName() //
				+ "(a.userName, a.active, a.userRole )" + " from " + Account.class.getName() + " a ";
//
		Session session = this.sessionFactory.getCurrentSession();
		Query<AccountInfo> query = session.createQuery(sql, AccountInfo.class);
		
		
		return new PaginationResult<AccountInfo>(query, page, maxResult, maxNavigationPage);
	}
}