package com.test.sku.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class OracleJDBC 
{
	 static enum MENU{ADD,LIST,FIND,UPDATE,JOIN,DELETE,EXIT};

	static Scanner kbd = new Scanner(System.in);
	//EmpDAO dao = new EmpDAO();
	static EmpDAO2 dao = new EmpDAO2();
   public static void main(String[] args) 
   {	   
	   boolean go = true;
	      while(go) {
	         switch(showMenu()) {
	         case ADD:       add();       break;
	         case LIST:      page();      break;
	         case FIND:      findByName();      break;
	         case UPDATE:    updateSal();    break;
	         case JOIN: 	join();			break;
	         case DELETE:    findByName();   break;
	         case EXIT:       go=false;   break;
	         }
	      }
	      System.out.println("프로그램 종료");
	

   }
   
	 private static void page() {
		 System.out.println("첫 시작 페이지: ");
		 int page = kbd.nextInt(); 
		 System.out.println("마지막 페이지: ");
		 int ipp = kbd.nextInt();
		 System.out.println("번호\t이름\t월급\t부서\t직업\t\t고용일");
		   List<EmpVO> list = dao.getPage(page,ipp);
		   for(EmpVO emp : list) {
			   System.out.println(emp);
		   }
		
	 }
	 static MENU showMenu() {
	     System.out.print("추가(a), 목록(s), 검색(f), 수정(u), 조인결과(j) 삭제(d), 종료(x):");
	     String m = kbd.nextLine().trim();
	     MENU menu = null;
	     switch(m) {
	     case "a": menu = MENU.ADD;       break;
	     case "s": menu = MENU.LIST;    break;
	     case "f": menu = MENU.FIND;    break;
	     case "u": menu = MENU.UPDATE;    break;
	     case "j": menu = MENU.JOIN; 	break;
	     case "d": menu = MENU.DELETE;    break;
	     case "x": menu = MENU.EXIT;    break;
	     }
	     return menu;
	  }

/**리스트 불러오기*/ 
   private static void getList() {	 //commit기준 
	   System.out.println("번호\t이름\t월급\t부서\t직업\t\t고용일");
	   List<EmpVO> list = dao.getList();
	   for(EmpVO emp : list) {
		   System.out.println(emp);
	   }
   }
 /**sal 변경*/
   private static void updateSal() {
	   System.out.println("사원 새급여액:");
	   int empno = kbd.nextInt();
	   int newSal = kbd.nextInt(); kbd.nextLine();
	   
	   EmpVO emp = new EmpVO();
	   emp.setEmpno(empno);
	   emp.setSal(newSal);

	   
	   boolean updated = dao.updateSal(emp);
	   System.out.println("수정결과:" + updated);
   }
   
 /**추가하기 empno,ename,deptno,sal,hiredate,job*/
   private static void add() {
	 
	   System.out.println("사번:");
	      int empno = kbd.nextInt();      kbd.nextLine();
	      System.out.println("이름:");
	      String ename = kbd.nextLine().trim();
	      System.out.println("부서번호:");
	      int deptno = kbd.nextInt();     kbd.nextLine();
	      System.out.println("급여:");
	      int sal = kbd.nextInt();        kbd.nextLine();
	      System.out.println("입사일:");
	      String sDate = kbd.nextLine().trim();
	      System.out.println("직무:");
	      String job = kbd.nextLine().trim();
	      
	      EmpVO emp = new EmpVO();
	      emp.setEmpno(empno);
	      emp.setEname(ename);
	      emp.setDeptno(deptno);
	      emp.setSal(sal);
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	      java.sql.Date hiredate = null;
	      try {
	         hiredate = new java.sql.Date(sdf.parse(sDate).getTime());
	      }catch(Exception e) {
	         e.printStackTrace();
	      }
	      emp.setHiredate(hiredate);
	      emp.setJob(job);
	      
	      boolean added = dao.add(emp);
	      if(added) System.out.println("추가 성공");
	      else System.err.println("추가 실패");

   }
   
 /**직업으로 찾기*/
   private static void findByName() {	 
	   System.out.println("찾으실 직업:");
	   String job = kbd.nextLine();
	   List<EmpVO> list = dao.findByJob(job);
	   for(EmpVO emp2 : list) {
		   System.out.println(emp2);
	   }
   }
 /*  
   private static void find() {
      System.out.print("검색대상 사번:");
      int empno = kbd.nextInt();     kbd.nextLine();
      EmpVO emp = dao.findByEmpno(empno);
      System.out.println(emp==null ? "검색실패":emp.toString());
   }
*/
   private static void join() {
	   System.out.println("부서명:");
	   int deptno = kbd.nextInt(); kbd.nextLine();
	   
	   List<Map<String,String>> list = dao.getJoinResult(deptno);
	   System.out.println("이름\t사번\t부서\t부서명\t\t호봉");
	   for(Map<String, String> map : list) {
		   System.out.printf("%s\t%s\t%s\t%s\t%s\n",
				   map.get("ENAME"), map.get("EMPNO"), map.get("DEPTNO"), map.get("DNAME"),map.get("GRADE"));
		   /* String sEname = map.get("ENAME");
		    * String ename = map.get("EMPNO");
		    * String deptno = map.get("DEPTNO");
		    * String dname = map.get("DNAME");
		    * String grade = map.get("GRADE");
		    * String line = String.format
		    */
	   }
	   			   
   }
     
 
   /**번호로 삭제하기*/
   private static void deleteByNum() {
	   System.out.println("삭제할 번호:");
	   int empno = kbd.nextInt(); kbd.nextLine();
	   
	   boolean deleted = dao.deleteByEmpno(empno);
	   System.out.println("삭제결과:" + deleted);
   }
}
