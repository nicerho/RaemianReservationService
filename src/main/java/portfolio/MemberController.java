package portfolio;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Controller
@RequestMapping("/mp")
public class MemberController {
	@Resource(name = "member")
	private MemberModule memberModule;
	@Resource(name = "notice")
	private NoticeModule noticeModule;
	private String service_id = "ncp:sms:kr:318002283590:raemian";
	private String access_id = "r7CvGlRfIoILTf14q8kj";
	private String security_key = "PT351QPM4f43dJQ1ZBiUkf2efETeYJ5nJLWrxhkV";
	private String service = "SMS";
	private String url = "https://sens.apigw.ntruss.com/sms/v2/services/" + service_id + "/messages";
	private String url2 = "/sms/v2/services/" + service_id + "/messages";
	private String timestamp = Long.toString(System.currentTimeMillis());

	@PostMapping("memberIdCheck")
	public String memberIdCheck(@RequestParam(required = false) String mid, MemberDTO md, Model model) {
		md = memberModule.memberIdCheck(mid);
		try {
			if (md == null) {
				model.addAttribute("mid", "no");
			} else {
				model.addAttribute("mid", "yes");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("mid", "error");
		}
		return "/mp/idCheckResult";
	}

	@PostMapping("/memberSubmit")
	public String adminSubmmit(@ModelAttribute("member") MemberDTO md, @RequestParam String mpw) {
		memberModule.memberSubmit(mpw, md);
		return "redirect:mainpage";
	}

	@RequestMapping("/mainpage")
	public String index(Model model) {
		Date nowDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String date = simpleDateFormat.format(nowDate);
		List<InfoDTO> info = memberModule.indexInfo();
		model.addAttribute("info", info);
		model.addAttribute("date", date);
		return "/mp/index";
	}

	@PostMapping("/memberLogin")
	public String memberLogin(@RequestParam String loginId, @RequestParam String loginPw, Model model,
			HttpServletRequest req) {
		Map<String, Object> map = memberModule.login(loginId, loginPw);
		if (map.containsKey("loginMember") == true) {
			HttpSession session = req.getSession();
			session.setAttribute("loginMember", map.get("loginMember"));
			model.addAttribute("loginMember", map.get("loginMember"));
		}
		model.addAttribute("map", map);
		return "/mp/loginResult";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "redirect:mainpage";
	}

	@GetMapping("/reserve")
	public String reservePage(String mid, ReserveDTO rd, Model model) {
		rd = memberModule.reserveCheck(rd, mid);
		model.addAttribute("reserve", rd);
		return "/mp/reservationIn";
	}

	@PostMapping("/reserves")
	public String reserve(ReserveDTO rd) {
		memberModule.insertReserve(rd);
		return "redirect:mainpage";
	}

	@RequestMapping("/reserveResult")
	public String reserveResult(ReserveDTO rd, Model model, String mid) {
		rd = memberModule.reserveCheck(rd, mid);
		model.addAttribute("reserve", rd);
		return "/mp/reserveCk";
	}

	@GetMapping("/reserveCheck")
	public void reserveCheck(ReserveDTO rd, String mid, HttpServletResponse res) throws IOException {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter pw = res.getWriter();
		rd = memberModule.reserveCheck(rd, mid);
		if (rd == null) {
			pw.write("<script>alert('에약확인은 전방문예약을 한 고객에 한해서만 가능합니다');location.href='./mainpage'</script>");
		} else {
			pw.write("<script>location.href='./reserveResult?mid=" + mid + "'</script>");
		}
		pw.flush();
		pw.close();
	}
	@GetMapping("/reserveCheck2")
	public void reserveCheck2(ReserveDTO rd, String mid, HttpServletResponse res) throws IOException {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter pw = res.getWriter();
		rd = memberModule.reserveCheck(rd, mid);
		if (rd == null) {
			pw.write("<script>alert('에약취소는 사전방문예약을 한 고객에 한해서만 가능합니다');location.href='./mainpage'</script>");
		} else {
			pw.write("<script>location.href='./reserveCancle?mid=" + mid + "'</script>");
		}
		pw.flush();
		pw.close();
	}

	@PostMapping("/changeReserve")
	public void changeReserve(ReserveDTO rd, String mid, HttpServletResponse res) throws IOException {
		res.setContentType("text/html; charset=utf-8");
		PrintWriter pw = res.getWriter();
		if (rd.getRchange() == "Y") {
			pw.write("<script>alert('사전방문예약을 변경하신 회원은 다시 방문예약을 변경하실 수 없습니다.');location.href='./mainpage'</script>");
		} else {
			memberModule.changeReserve(rd);
			pw.write("<script>alert('변경사항이 성공적으로 저장되었습니다.');location.href='./mainpage'</script>");
		}
		pw.flush();
		pw.close();
	}
	
	@GetMapping("/reserveCancle")
	public String reserveCancle(String mid, ReserveDTO rd, Model model) {
		rd = memberModule.reserveCheck(rd, mid);
		model.addAttribute("reserve", rd);
		return "/mp/reservationCancle";
	}
	@RequestMapping("/reserveDelete")
	public void reserveDelete(String rid, HttpServletResponse res) throws IOException{
		
		res.setContentType("text/html; charset=utf-8");
		PrintWriter pw = res.getWriter();
		System.out.println(rid);
		System.out.println("test");
		memberModule.deleteReserve(rid);
		pw.write("<script>alert('변경사항이 성공적으로 저장되었습니다.');location.href='./mainpage'</script>");
		pw.flush();
		pw.close();
	}
	@RequestMapping("/notice")
	public String noticeConfig(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "search", required = false) String search, Model model) {
		int pageSize = 10; // 페이지당 표시할 게시물 수
		List<NoticeDTO> notices = noticeModule.getNoticesByPage(pageNumber, pageSize, search);
		int totalNotices = noticeModule.countNotices(search);
		int totalPages = (int) Math.ceil((double) totalNotices / pageSize);
		model.addAttribute("notices", notices);
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("search", search);
		
		return "/mp/notice";
	}
	@GetMapping("/faq")
	public String faqPage(Model model) {
		List<FaqDTO> list = memberModule.memberFaq();
		model.addAttribute("faqs",list);
		return "/mp/faq";
	}
	// SMS 발송
		@PostMapping("/smsok")
		@SuppressWarnings({ "unchecked", "deprecation" })
		public void smsOk(@RequestParam(required = false) String mtel, @RequestParam(required = false) String number,HttpServletResponse response)
				throws IOException, Exception {
			System.out.println(mtel);
			System.out.println(number);
			JSONObject code1 = new JSONObject();
			JSONObject code2 = new JSONObject();
			JSONArray code3 = new JSONArray();
			code1.put("type", service);
			code1.put("countryCode", "82");
			code1.put("from", "01033419230");
			code1.put("contentType", "COMM");
			code1.put("content", "인증 번호 발송"); // 관리자가 확인하는 메세지 내용
			code2.put("content", number); // 클라이언트가 받을 메세지 내용
			code2.put("to", mtel);
			code3.add(code2);
			code1.put("messages", code3);
			String data = code1.toString();
			OkHttpClient client = new OkHttpClient();
			System.out.println(data);
			PrintWriter pw = response.getWriter();
			Request req = new Request.Builder()
					.addHeader("x-ncp-apigw-timestamp", timestamp)
					.addHeader("x-ncp-iam-access-key", access_id)
					.addHeader("x-ncp-apigw-signature-v2", makese())
					.url(url)
					.post(RequestBody.create(MediaType.parse("application/json;"), data))
					.build();
			Response res = client.newCall(req).execute();
			
			String result = res.body().string();
			if(result.indexOf("202")>0) {
				pw.write("ok");
			}else {
				pw.write("error");
			}
			System.out.println(req);
			System.out.println(result);

		}
		// 코드 암호화 Base64 또는 SHA ~~
		public String makese() throws Exception{
			String sp = " ";
			String line = "\n";
			String msg = new StringBuilder()
					.append("POST")
					.append(sp)
					.append(url2)
					.append(line)
					.append(timestamp)
					.append(line)
					.append(access_id).toString();
		
			String base64 = "";
		
			SecretKeySpec key = new SecretKeySpec(security_key.getBytes("UTF-8"), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
			byte[] rawHmac = mac.doFinal(msg.getBytes("UTF-8"));
			base64 = Base64.getEncoder().encodeToString(rawHmac);
		
			return base64;
		}
}
