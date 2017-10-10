/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.email;

/**
 * This interface defines the base functionality and interface for e-mail access. 
 * Different implementations are envisioned for different mechanisms and libraries 
 * for sending e-mail.
 */
public interface EmailClient
{
	/**
	 * Template tag representing a GitHub login used in e-mail messages.
	 */
	public static final String TEMPLATE_TAG_LOGIN = "{{login}}";
	
	/**
	 * Sends an e-mail.
	 * @param from - Sender e-mail address, e.g., samiam@byu.edu.
	 * @param to - Recipient e-mail address.
	 * @param subject - E-mail subject.
	 * @param htmlBody - HTML formatted message body.
	 * @param textBody - Plain text formatted message body.
	 */
	public void send( String from, String to, String subject, String htmlBody, String textBody );
	
	/**
	 * Default implementation for a template tag substitution method. This method replaces all occurrences
	 * of <code>templateTag</code> in <code>srcTxt</code> with <code>replacementTxt</code>. <em>Note: The source string is not
	 * modified! A new modified string is returned.</em>
	 * <p>
	 * Note: In a real production system a more complex templating engine would be used.
	 * @param srcTxt - Source string.
	 * @param templateTag - Tag presumably found in <code>srcTxt</code> to be replaced <code>replacementTxt</code>. 
	 * @param replacementTxt - Text with which to replace the <code>templateTag</code> in <code>srcTxt</code>.
	 * @return
	 */
	default String replaceTemplateText( String srcTxt, String templateTag, String replacementText )
	{
		StringBuilder sb = new StringBuilder( srcTxt.length() );

		int startIdx = 0;
		int endIdx = srcTxt.length();
		for(;;)
		{
			// find the start of the tag / end of string segment
			endIdx = srcTxt.indexOf( templateTag, startIdx );
			if( endIdx == -1 )
			{
				// no more tags to be replaced
				break;
			}
			
			// append the next string segment to the output collector, tack on the replacement text,
			// advance the start index beyond the tag
			sb.append( srcTxt.substring( startIdx, endIdx ) );
			sb.append( replacementText );
			startIdx = endIdx + templateTag.length();
		}

		// this appends the last segement or the whole string if there are no tags.
		endIdx = srcTxt.length();
		sb.append( srcTxt.substring( startIdx, endIdx ) );
		
		return sb.toString();		
	}
}
