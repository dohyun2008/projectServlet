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
		
		// ���� ���ε� ��� ����
		String root = session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"photo";
		File f = new File(pathname);
		// ��ΰ� �������� ������ �����
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
		// �г��� �ߺ� üũ
		MemberDAO dao = new MemberDAO();
		// input���� �г��� �޾ƿ�
		String nick = req.getParameter("nick");
		
		// �г������� ����� ���� ����
		MemberDTO dto = dao.readNick(nick);
		String passed = "true";
		if(dto != null)
			passed = "false";
		
		// JSON���·� passed ����
		JSONObject job = new JSONObject();
		job.put("passed", passed);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		out.print(job.toString());		
	}
	
	private void chkId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ���̵� �ߺ� üũ
		MemberDAO dao = new MemberDAO();
		// ���̵� �Է¶����� id���� parameter�� �Ѱܹ޴´�
		String id = req.getParameter("id");
		
		// �޾ƿ� id������ DB���� �ش��ϴ� ������ �д´�.
		MemberDTO dto = dao.readMember(id);
		// ���̵� �ߺ� ���θ� ��Ÿ���� ���� ���� passed
		String passed = "true";
		
		// id������ �о�� ������ ������, �� �ش��ϴ� ID�� �����ϸ�
		if(dto != null)
			passed = "false";
		
		// JSON ���·� passed ����
		JSONObject job = new JSONObject();
		job.put("passed", passed);
		
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		out.print(job.toString());
	}

	private void loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ������ ����ȭ������ �̵�(�α���)
		String path="/WEB-INF/views/layout/main.jsp";
		forward(req, resp, path);
	}
	private void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �α��� ó��
		HttpSession session = req.getSession();
		MemberDAO dao = new MemberDAO();
		
		// �α��� modal���� id, pw �޾ƿ�
		String userId = req.getParameter("userId");
		String userPw = req.getParameter("userPw");
		
		String state = "false";

		// ��ġ�ϴ� ������ ������
		MemberDTO dto = dao.readMember(userId);
		if(dto != null) {
			if(userPw.equals(dto.getPw())) {
				// �α��� ���� : �α��������� ������ ����
				session.setMaxInactiveInterval(1800);
				// ���ǿ� ������ ����
				SessionInfo info=new SessionInfo();
				info.setUserId(dto.getId());
				info.setUserNick(dto.getNick());
				
				// ���ǿ� member�̶�� �̸����� ����
				session.setAttribute("member", info);
				
				// state�� true�� ����
				state = "true";
			}
		}
		
		// JSON �������� ������ ����
		JSONObject job = new JSONObject();
		resp.setContentType("text/html;charset=utf-8");
		job.put("state", state);
		PrintWriter out = resp.getWriter();
		out.print(job.toString());
	}
	
	private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �α׾ƿ�
		HttpSession session=req.getSession();
		String cp=req.getContextPath();

		// ���ǿ� ����� ���� ����
		session.removeAttribute("member");
				
		session.invalidate();
				
		resp.sendRedirect(cp+"/mem/login.do");
	}
	private void memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ������ �� �̵�
		req.setAttribute("mode", "created");
		req.setAttribute("title", "ȸ �� �� �� ");
		String path="/WEB-INF/views/member/member.jsp";
		forward(req, resp, path);
		
	}
	private void memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ������
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = new MemberDTO();
		String cp = req.getContextPath();
		
		String encType = "utf-8";
		int maxSize = 1024*1024*5;
		
		// ������ ���� ���ε带 ���� MultipartRequest ���
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
		// ȸ���������� and ȸ������ ȭ�� ������ ���� title�� mode�� setAttribute
		req.setAttribute("title", "ȸ �� �� ��");
		req.setAttribute("mode", "reg");
		
		resp.sendRedirect(cp+"/mem/login.do");
	}
	
	private void pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ��й�ȣ Ȯ�� ȭ�� �̵�
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		// �α��� �ȵǾ������� ����ȭ������ ���ư�
		if(info == null) {
			resp.sendRedirect(cp+"/mem/login.do");
			return;
		}
		// mode�� ���� ȭ���� ���� ����
		String mode = req.getParameter("mode");
		if(mode.equals("update")) {
			req.setAttribute("title", "ȸ �� �� �� �� ��");
		} else {
			req.setAttribute("title", "ȸ �� Ż ��");
		}
		
		// pwd.jsp�� ������
		req.setAttribute("mode", mode);
		forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
		
		
	}
	private void pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �н����� Ȯ��
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
		// pw ���� mode �� �޾ƿ�
		String pw = req.getParameter("pw");
		String mode = req.getParameter("mode");
		
		// ��й�ȣ�� ��ġ���� ������
		if(! dto.getPw().equals(pw)) {
			if(mode.equals("update"))
				req.setAttribute("title", "ȸ �� �� �� �� ��");
			else
				req.setAttribute("title", "ȸ �� Ż ��");
			req.setAttribute("mode", mode);
			
			resp.setContentType("text/html; charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<script>alert('��й�ȣ�� ��ġ���� �ʽ��ϴ�.'); history.go(-1);</script>");
            out.flush();

			return;
		}
		
		// ȸ�� Ż��
		if(mode.equals("delete")) {
			dao.deleteMember(info.getUserId());
			session.removeAttribute("member");
			session.invalidate();
			
			resp.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = resp.getWriter();
	        out.println("<script>alert('Ż�� �Ϸ�Ǿ����ϴ�.');javascript:location.href='"+cp+"/mem/login.do';</script>");
	        out.flush();
		} else if(mode.equals("update")) {
			// ���� ������ �̵�
			req.setAttribute("title", "ȸ�� ���� ���� ");
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "update");
			forward(req, resp, "/WEB-INF/views/member/member.jsp");	
		}
	}
	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ�� ���� ����
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
			// ���� ���� â���� �޾ƿ� �̸�
			String saveFilename = mreq.getFilesystemName("pic");
			// ���ϸ� �缳��
			saveFilename = FileManager.doFilerename(pathname, saveFilename);
			dto.setPic(saveFilename);
		} else {
			dto.setPic(pic);
		}
		
		// ������ ����
		dao.updateMember(dto);
		
		//Ȯ�ο�
		resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        // ��ũ �����ʷ� �����ؾ���******
        out.println("<script>alert('������ �Ϸ�Ǿ����ϴ�.');javascript:location.href='"+cp+"/board/profile.do?id="+info.getUserId()+"';</script>");
        out.flush();
		
	}
}
