package sns.member;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import sns.member.MemberDAO;
import sns.member.SessionInfo;

import net.sf.json.JSONObject;
import sns.util.FileManager;
import sns.util.MyServlet;

@WebServlet("/mem/*")
public class MemberServlet extends MyServlet {

	private static final long serialVersionUID = 1L;
	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String header = req.getHeader("AJAX");
		if(header != null && header.equals("true") && info == null) {
			resp.sendError(403);
			return;
		}
		
		// 파일 업로드 경로 지정
		String root = session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"photo";
		File f = new File(pathname);
		// 경로가 존재하지 않으면 만든다
		if(! f.exists()) {
			f.mkdirs();
		}
		
		if(uri.indexOf("login.do")!=-1) {
			loginForm(req, resp);
		} else if(uri.indexOf("login_ok.do")!=-1) {
			loginSubmit(req, resp);
		} else if(uri.indexOf("logout.do")!=-1) {
			logout(req, resp);
		} else if(uri.indexOf("member.do")!=-1) {
			memberForm(req, resp);
		} else if(uri.indexOf("member_ok.do")!=-1) {
			memberSubmit(req, resp);
		} else if(uri.indexOf("pwd.do")!=-1) {
			pwdForm(req, resp);
		} else if(uri.indexOf("pwd_ok.do")!=-1) {
			pwdSubmit(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("chkId.do")!=-1) {
			chkId(req, resp);
		} else if(uri.indexOf("chkNick.do") != -1) {
			chkNick(req, resp);
		} else if(uri.indexOf("follow.do") != -1) {
			follow(req, resp);
		}
	}

	private void follow(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberDAO dao = new MemberDAO();

		String state = "false";
		String fan = req.getParameter("fan");
		String star = req.getParameter("star");
		
		int result = dao.follow(fan, star);
		if(result == 1) {
			state = "true";
		} else if (result == 0) {
			state = "false";
		}
		
		JSONObject job = new JSONObject();
		job.put("state", state);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out=resp.getWriter();
		out.print(job.toString());
		
	}

	private void chkNick(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 닉네임 중복 체크
		MemberDAO dao = new MemberDAO();
		// input에서 닉네임 받아옴
		String nick = req.getParameter("nick");
		
		// 닉네임으로 사용자 정보 읽음
		MemberDTO dto = dao.readNick(nick);
		String passed = "true";
		if(dto != null)
			passed = "false";
		
		// JSON형태로 passed 전송
		JSONObject job = new JSONObject();
		job.put("passed", passed);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		out.print(job.toString());		
	}
	
	private void chkId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 아이디 중복 체크
		MemberDAO dao = new MemberDAO();
		// 아이디 입력란에서 id값을 parameter로 넘겨받는다
		String id = req.getParameter("id");
		
		// 받아온 id값으로 DB에서 해당하는 정보를 읽는다.
		MemberDTO dto = dao.readMember(id);
		// 아이디 중복 여부를 나타내기 위한 변수 passed
		String passed = "true";
		
		// id값으로 읽어온 정보가 있으면, 즉 해당하는 ID가 존재하면
		if(dto != null)
			passed = "false";
		
		// JSON 형태로 passed 전송
		JSONObject job = new JSONObject();
		job.put("passed", passed);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		out.print(job.toString());
	}

	private void loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 페이지 시작화면으로 이동(로그인)
		String path="/WEB-INF/views/layout/main.jsp";
		forward(req, resp, path);
	}
	private void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그인 처리
		HttpSession session = req.getSession();
		MemberDAO dao = new MemberDAO();
		
		// 로그인 modal에서 id, pw 받아옴
		String userId = req.getParameter("userId");
		String userPw = req.getParameter("userPw");
		
		String state = "false";

		// 일치하는 정보가 있으면
		MemberDTO dto = dao.readMember(userId);
		if(dto != null) {
			if(userPw.equals(dto.getPw())) {
				// 로그인 성공 : 로그인정보를 서버에 저장
				session.setMaxInactiveInterval(1800);
				// 세션에 저장할 내용
				SessionInfo info=new SessionInfo();
				info.setUserId(dto.getId());
				info.setUserNick(dto.getNick());
				
				// 세션에 member이라는 이름으로 저장
				session.setAttribute("member", info);
				
				// state는 true로 변경
				state = "true";
			}
		}
		
		// JSON 형식으로 데이터 전송
		JSONObject job = new JSONObject();
		resp.setContentType("text/html;charset=utf-8");
		job.put("state", state);
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}
	
	private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그아웃
		HttpSession session=req.getSession();
		String cp=req.getContextPath();

		// 세션에 저장된 정보 제거
		session.removeAttribute("member");
				
		session.invalidate();
				
		resp.sendRedirect(cp+"/mem/login.do");
	}
	private void memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 폼 이동
		req.setAttribute("mode", "created");
		req.setAttribute("title", "회 원 가 입 ");
		String path="/WEB-INF/views/member/member.jsp";
		forward(req, resp, path);
		
	}
	private void memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = new MemberDTO();
		String cp = req.getContextPath();
		
		String encType = "utf-8";
		int maxSize = 1024*1024*5;
		
		// 프로필 사진 업로드를 위해 MultipartRequest 사용
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		
		dto.setId(mreq.getParameter("id"));
		
		String saveName = mreq.getFilesystemName("pic");
		saveName = FileManager.doFilerename(pathname, saveName);
		dto.setPic(saveName);

		dto.setPw(mreq.getParameter("pw"));

		dto.setName(mreq.getParameter("name"));

		dto.setNick(mreq.getParameter("nick"));

		dto.setEmail(mreq.getParameter("email"));

		dto.setTel(mreq.getParameter("tel"));

		dto.setBirth(mreq.getParameter("birth"));

		dao.insertMember(dto);
		// 회원정보수정 and 회원가입 화면 구분을 위해 title과 mode를 setAttribute
		req.setAttribute("title", "회 원 가 입");
		req.setAttribute("mode", "reg");
		
		resp.sendRedirect(cp+"/mem/login.do");
	}
	
	private void pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 비밀번호 확인 화면 이동
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		// 로그인 안되어있으면 시작화면으로 돌아감
		if(info == null) {
			resp.sendRedirect(cp+"/mem/login.do");
			return;
		}
		// mode에 따라 화면의 제목 구분
		String mode = req.getParameter("mode");
		if(mode.equals("update")) {
			req.setAttribute("title", "회 원 정 보 수 정");
		} else {
			req.setAttribute("title", "회 원 탈 퇴");
		}
		
		// pwd.jsp로 포워딩
		req.setAttribute("mode", mode);
		forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
		
		
	}
	private void pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String cp=req.getContextPath();
		
		MemberDAO dao=new MemberDAO();
		
		if(info==null) {
			resp.sendRedirect(cp+"/mem/login.do");
			return;
		}
		
		MemberDTO dto = dao.readMember(info.getUserId());
		if(dto == null) {
			session.invalidate();
			resp.sendRedirect(cp+"/mem/login.do");
			return;
		}
		// pw 값과 mode 값 받아옴
		String pw = req.getParameter("pw");
		String mode = req.getParameter("mode");
		
		// 비밀번호가 일치하지 않으면
		if(! dto.getPw().equals(pw)) {
			if(mode.equals("update"))
				req.setAttribute("title", "회 원 정 보 수 정");
			else
				req.setAttribute("title", "회 원 탈 퇴");
			req.setAttribute("mode", mode);
			
			resp.setContentType("text/html; charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<script>alert('비밀번호가 일치하지 않습니다.'); history.go(-1);</script>");
            out.flush();

			return;
		}
		
		// 회원 탈퇴
		if(mode.equals("delete")) {
			dao.deleteMember(info.getUserId());
			session.removeAttribute("member");
			session.invalidate();
			
			resp.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = resp.getWriter();
	        out.println("<script>alert('탈퇴가 완료되었습니다.');javascript:location.href='"+cp+"/mem/login.do';</script>");
	        out.flush();
		} else if(mode.equals("update")) {
			// 수정 폼으로 이동
			req.setAttribute("title", "회원 정보 수정 ");
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "update");
			forward(req, resp, "/WEB-INF/views/member/member.jsp");	
		}
	}
	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원 정보 수정
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		MemberDAO dao = new MemberDAO();
		
		if(info==null) {
			resp.sendRedirect(cp+"/mem/login.do");
			return;
		}
		String encType = "utf-8";
		int maxSize = 1024*1024*5;
		
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		
		MemberDTO dto = new MemberDTO();
		
		dto.setId(info.getUserId());
		dto.setPw(mreq.getParameter("pw"));
		dto.setName(mreq.getParameter("name"));
		dto.setNick(mreq.getParameter("nick"));
		dto.setEmail(mreq.getParameter("email"));
		dto.setTel(mreq.getParameter("tel"));
		dto.setBirth(mreq.getParameter("birth"));
		
		info.setUserNick(dto.getNick());
		
		String pic = mreq.getParameter("pic");
		if(mreq.getFile("pic") != null) {
			FileManager.doFiledelete(pathname, pic);
			// 파일 선택 창에서 받아온 이름
			String saveFilename = mreq.getFilesystemName("pic");
			// 파일명 재설정
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			dto.setPic(saveFilename);
		} else {
			dto.setPic(pic);
		}
		
		// 데이터 수정
		dao.updateMember(dto);
		
		//확인용
		resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        // 링크 프로필로 수정해야함******
        out.println("<script>alert('수정이 완료되었습니다.');javascript:location.href='"+cp+"/board/profile.do?id="+info.getUserId()+"';</script>");
        out.flush();
		
	}
}
