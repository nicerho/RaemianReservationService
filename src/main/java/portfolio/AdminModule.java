package portfolio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository("adminsubmit")
public class AdminModule {
	@Resource(name = "sqlsession")
	private SqlSessionTemplate sqlsession;
	@Autowired
	private BCryptPasswordEncoder bp;

	public int adminSubmit(String apw, AdminDTO ad) {
		String pw = bp.encode(apw);
		ad.setApw(pw);
		System.out.println(ad.getAbranch());
		System.out.println(ad.getAemail());
		int a = sqlsession.insert("pfDB.adminInsert", ad);
		if (a > 0) {
			System.out.println("성공");
		} else {
			System.out.println("error");
		}
		return a;
	}

	public Map<String,Object> adminLogin(String login_id, String login_pass) {
		Map<String,Object> result = new HashMap<>();
		AdminDTO ad = sqlsession.selectOne("pfDB.login", login_id);
		if (ad==null) {
			result.put("error","noid");
		} else if (ad.getAuse().equals("N")) {
			result.put("error","nouse");
		} else {
			if (bp.matches(login_pass, ad.getApw()) == false && Integer.parseInt(ad.getAloginattempt()) < 5) {
					sqlsession.update("pfDB.changeAdminLoginAttempt",login_id);
					result.put("error","loginfail");
					result.put("attemps", ad.getAloginattempt());
			}else if(Integer.parseInt(ad.getAloginattempt()) == 5) {
				result.put("error", "overmaxattempt");
			}else if(bp.matches(login_pass, ad.getApw()) == true && Integer.parseInt(ad.getAloginattempt()) < 5) {
				result.put("error", "noerr");
				result.put("loginMember", ad);
				sqlsession.update("pfDB.changeAdminLoginAttemptTo0",login_id);
			}
		}
		
		return result;
	}

	public int page() {
		int x = sqlsession.selectOne("pfDB.adminCount");

		return x;
	}

	public AdminDTO idCheck(String aid) {
		AdminDTO ad = sqlsession.selectOne("pfDB.login", aid);
		return ad;
	}

	public void changeAdminConfigToY(String adminNumber) {
		sqlsession.update("pfDB.changeAdminConfigToY", adminNumber);

	}

	public void changeAdminConfigToN(String adminNumber) {
		sqlsession.update("pfDB.changeAdminConfigToN", adminNumber);
	}

	public int countAdmin(String searchpart, String adep, String search) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("searchpart", searchpart);
		parameters.put("adep", adep);
		parameters.put("search", search);
		return sqlsession.selectOne("pfDB.countAdmin", parameters);
	}

	public List<AdminDTO> getAdminByPage(int currentPage, int pageSize, String searchpart, String adep, String search) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("start", (currentPage - 1) * pageSize);
		parameters.put("pageSize", pageSize);
		parameters.put("searchpart", searchpart);
		parameters.put("adep", adep);
		parameters.put("search", search);
		List<AdminDTO> lists = sqlsession.selectList("pfDB.selectAdminByPage", parameters);
		return lists;
	}
	public int countMember(String searchpart, String search) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("searchpart", searchpart);
		parameters.put("search", search);
		return sqlsession.selectOne("pfDB.countMember", parameters);
	}

	public List<MemberDTO> getMemberByPage(int currentPage, int pageSize, String searchpart,  String search) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("start", (currentPage - 1) * pageSize);
		parameters.put("pageSize", pageSize);
		parameters.put("searchpart", searchpart);
		parameters.put("search", search);
		List<MemberDTO> lists = sqlsession.selectList("pfDB.selectMemberByPage", parameters);
		return lists;
	}
	public int deleteMember(String memberNumber) {
		return sqlsession.delete("pfDB.deleteMember",memberNumber);
	}
	public List<MemberDTO> getMemberByDate() {
		Date nowDate = new Date();
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    String date = simpleDateFormat.format(nowDate); 
		return sqlsession.selectList("pfDB.selectMemberByDate",date);
	}
	
}
