package com.tcs.tvmilp;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
@ParseClassName("ParseRegister")
public class ParseRegister extends ParseObject {

	public ParseRegister ()
	{
	
	}
	
	public ParseUser getOwner()
	{
		return getParseUser("owner");
	}
	
}
