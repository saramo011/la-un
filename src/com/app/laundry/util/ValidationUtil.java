package com.app.laundry.util;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class ValidationUtil {
	 
	
	
	public static boolean isEmail(String email) {
		boolean matchFound1;
		boolean returnResult = true;
		email = email.trim();
		if (email.equalsIgnoreCase(""))
			returnResult = false;
		else if (!Character.isLetter(email.charAt(0)))
			returnResult = false;
		else {
			Pattern p1 = Pattern.compile("^\\.|^\\@ |^_");
			Matcher m1 = p1.matcher(email.toString());
			matchFound1 = m1.matches();

			Pattern p = Pattern
					.compile("^[a-zA-z0-9._-]+[@]{1}+[a-zA-Z0-9]+[.]{1}+[a-zA-Z]{2,4}$");
			
			// Match the given string with the pattern
			Matcher m = p.matcher(email.toString());

			// check whether match is found
			boolean matchFound = m.matches();

			StringTokenizer st = new StringTokenizer(email, ".");
			String lastToken = null;
			while (st.hasMoreTokens()) {
				lastToken = st.nextToken();
			}
			if (matchFound && lastToken.length() >= 2
					&& email.length() - 1 != lastToken.length()
					&& matchFound1 == false) {

				returnResult = true;
			} else
				returnResult = false;
			
			if(email.length()>50)
				returnResult=false;
		}
		return returnResult;
	}
	
	
	
	
	
	public static boolean isNull(String text) {
		
		boolean returnResult = false;
		if(text==null)
			returnResult=true;
		if(text.length()==0)
			returnResult=true;
		return returnResult;
	}
	
	
	
	
	public static boolean checkPassWordAndConfirmPassword(String password,String confirmPassword) 
	 {
	     boolean pstatus = false;
	     if (confirmPassword != null && password != null) 
	     {
	       if (password.equals(confirmPassword)) 
	       {
	            pstatus = true;
	       } 
	     }
	    return pstatus;
	}
	
	
	public static class PasswordValidator{
		 
		  private Pattern pattern;
		  private Matcher matcher;
	 
		  private static final String PASSWORD_PATTERN = 
	              "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	 
		  public PasswordValidator(){
			  pattern = Pattern.compile(PASSWORD_PATTERN);
		  }
	 
		
		  public boolean validate(final String password){
	 
			  matcher = pattern.matcher(password);
			  return matcher.matches();
	 
		  }
	}
}
