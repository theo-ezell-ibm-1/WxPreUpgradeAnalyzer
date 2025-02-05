package WxPreUpgradeAnalyzer;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.lang.ns.NSNode;
import java.io.File;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// --- <<IS-END-IMPORTS>> ---

public final class util

{
	// ---( internal utility methods )---

	final static util _instance = new util();

	static util _newInstance() { return new util(); }

	static util _cast(Object o) { return (util)o; }

	// ---( server methods )---




	public static final void getCurrentPackageDirectory (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCurrentPackageDirectory)>> ---
		// @sigtype java 3.5
		IDataCursor pc = pipeline.getCursor();
		IDataUtil.put(pc, "packageDir", Paths.get("").toAbsolutePath().toString()+File.separator+"packages"+File.pathSeparator+Service.getPackageName());
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void getNodeAsDocument (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getNodeAsDocument)>> ---
		// @sigtype java 3.5
		// [i] object:0:required node
		// [o] record:0:required document
		IDataCursor pc = pipeline.getCursor();
		Object node = IDataUtil.get(pc, "node");
		try {
		
			IDataUtil.put(pc, "document", ((NSNode)node).getAsData());
		} catch (Exception e){
			throw new ServiceException(node.getClass().getName());
		}
		// --- <<IS-END>> ---

                
	}



	public static final void getUniqueValues (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getUniqueValues)>> ---
		// @sigtype java 3.5
		// [i] field:1:required strings
		// [o] field:1:required uniqueValues
		IDataCursor pc = pipeline.getCursor();
		String[] strings = IDataUtil.getStringArray(pc, "strings");
		ArrayList<String> uniqueValues = new ArrayList<String>();
		for (String s : strings){
			if(!uniqueValues.contains(s)){
				uniqueValues.add(s);
			}
		}
		IDataUtil.put(pc, "uniqueValues", uniqueValues.toArray(new String[0]));
		// --- <<IS-END>> ---

                
	}



	public static final void isSameOrHigherVersion (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(isSameOrHigherVersion)>> ---
		// @sigtype java 3.5
		// [i] field:0:required version
		// [i] field:0:required versionToCheck
		// [o] field:0:required isSameOrHigherVersion
		String isSameOrHigherVersion = "false";
		IDataCursor pc = pipeline.getCursor();
		String version = IDataUtil.getString(pc, "version");
		String versionToCheck = IDataUtil.getString(pc, "versionToCheck");
		if(version.equalsIgnoreCase(versionToCheck)){
			isSameOrHigherVersion = "true";
		} else {
			String[] v = version.split("\\.");
			String[] vtc = versionToCheck.split("\\.");
			for (int i=0; i<v.length; i++ ){
				if(Integer.valueOf(v[i]) < Integer.valueOf(vtc[i])){
					isSameOrHigherVersion = "true";
					break;
				}
			}
		}
		IDataUtil.put(pc, "isSameOrHigherVersion", isSameOrHigherVersion);
		// --- <<IS-END>> ---

                
	}



	public static final void isStringInList (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(isStringInList)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inString
		// [i] field:1:required inStringList
		// [o] field:0:required stringInList
		IDataCursor idc = pipeline.getCursor();
		String inString = IDataUtil.getString(idc,"inString");
		ArrayList<String> inStringArray = new ArrayList<String>(Arrays.asList(IDataUtil.getStringArray(idc,"inStringList")));
		
		IDataUtil.put(idc, "stringInList", String.valueOf(inStringArray.contains(inString)));
		idc.destroy();
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void matchesRegex (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(matchesRegex)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inString
		// [i] field:0:required regex
		// [o] field:0:required matches {"true","false"}
		IDataCursor pc = pipeline.getCursor();
		String inString = IDataUtil.getString(pc, "inString");
		String regex = IDataUtil.getString(pc, "regex");
		if(regex.length()<1){
			IDataUtil.put(pc, "matches", "true");
		} else {
			IDataUtil.put(pc, "matches", Boolean.toString(Pattern.matches(regex, inString)));
		}
		pc.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void stringListToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stringListToString)>> ---
		// @sigtype java 3.5
		// [i] field:1:required stringList
		// [i] field:0:required delimiter
		// [o] field:0:required outString
		IDataCursor pc = pipeline.getCursor();
		String[] stringList = IDataUtil.getStringArray(pc, "stringList");
		String delimiter = IDataUtil.getString(pc, "delimiter");
		StringBuilder outString = new StringBuilder();
		for(String string : stringList){
			outString.append(string+delimiter);
		}
		IDataUtil.put(pc, "outString", outString.toString());
		pc.destroy();
		
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static String[] splitString(String s){
		ArrayList<String> strList = new ArrayList<String>();
		String sub = new String();
		int start = 0;
		int end = 0;
		int count = 0;
		while(start > -1){
	//			start = s.indexOf("~~pub.");
			end = indexOfByRegex("~~pub\\.[^\\s]+\\s", s);
			if (end < 0){
				sub = s.substring(start);
				break;
			} else {
				sub = s.substring(start, end);
			}
			strList.add(sub);
			s = s.replaceFirst(sub, "");
			if (count++>10){
				break;
			}
		}
		return strList.toArray(new String[0]);
	}
	
	public static int indexOfByRegex(String pattern, String text) {
		Pattern p = Pattern.compile(pattern);
	    Matcher m = p.matcher(text);
	    if ( m.find() ) {
	        return m.start(); 
	    }
	    return -1;
	}
	// --- <<IS-END-SHARED>> ---
}

