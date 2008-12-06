/*
 * XMLException.java NanoXML/Java $Revision: 1.4 $ $Date: 2002/01/04 21:03:29 $
 * $Name: RELEASE_2_2_1 $ This file is part of NanoXML 2 for Java. Copyright (C)
 * 2000-2002 Marc De Scheemaecker, All Rights Reserved. This software is
 * provided 'as-is', without any express or implied warranty. In no event will
 * the authors be held liable for any damages arising from the use of this
 * software. Permission is granted to anyone to use this software for any
 * purpose, including commercial applications, and to alter it and redistribute
 * it freely, subject to the following restrictions: 1. The origin of this
 * software must not be misrepresented; you must not claim that you wrote the
 * original software. If you use this software in a product, an acknowledgment
 * in the product documentation would be appreciated but is not required. 2.
 * Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software. 3. This notice may not be
 * removed or altered from any source distribution.
 */
package org.freeplane.io.xml.n3.nanoxml;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An XMLException is thrown when an exception occurred while processing the XML
 * data.
 * 
 * @author Marc De Scheemaecker
 * @version $Name: RELEASE_2_2_1 $, $Revision: 1.4 $
 */
public class XMLException extends Exception {
	/**
	 * Builds the exception message
	 * 
	 * @param systemID
	 *            the system ID from where the data came
	 * @param lineNr
	 *            the line number in the XML data where the exception occurred.
	 * @param e
	 *            the encapsulated exception.
	 * @param msg
	 *            the message of the exception.
	 * @param reportParams
	 *            true if the systemID, lineNr and e params need to be appended
	 *            to the message
	 */
	private static String buildMessage(final String systemID, final int lineNr,
	                                   final Exception e, final String msg,
	                                   final boolean reportParams) {
		String str = msg;
		if (reportParams) {
			if (systemID != null) {
				str += ", SystemID='" + systemID + "'";
			}
			if (lineNr >= 0) {
				str += ", Line=" + lineNr;
			}
			if (e != null) {
				str += ", Exception: " + e;
			}
		}
		return str;
	}

	/**
	 * Encapsulated exception.
	 */
	private Exception encapsulatedException;
	/**
	 * The line number in the XML data where the exception occurred.
	 */
	final private int lineNr;
	/**
	 * The message of the exception.
	 */
	final private String msg;
	/**
	 * The system ID of the XML data where the exception occurred.
	 */
	private String systemID;

	/**
	 * Creates a new exception.
	 * 
	 * @param e
	 *            the encapsulated exception.
	 */
	public XMLException(final Exception e) {
		this(null, -1, e, "Nested Exception", false);
	}

	/**
	 * Creates a new exception.
	 * 
	 * @param msg
	 *            the message of the exception.
	 */
	public XMLException(final String msg) {
		this(null, -1, null, msg, false);
	}

	/**
	 * Creates a new exception.
	 * 
	 * @param systemID
	 *            the system ID of the XML data where the exception occurred
	 * @param lineNr
	 *            the line number in the XML data where the exception occurred.
	 * @param e
	 *            the encapsulated exception.
	 */
	public XMLException(final String systemID, final int lineNr,
	                    final Exception e) {
		this(systemID, lineNr, e, "Nested Exception", true);
	}

	/**
	 * Creates a new exception.
	 * 
	 * @param systemID
	 *            the system ID from where the data came
	 * @param lineNr
	 *            the line number in the XML data where the exception occurred.
	 * @param e
	 *            the encapsulated exception.
	 * @param msg
	 *            the message of the exception.
	 * @param reportParams
	 *            true if the systemID, lineNr and e params need to be appended
	 *            to the message
	 */
	public XMLException(final String systemID, final int lineNr,
	                    final Exception e, final String msg,
	                    final boolean reportParams) {
		super(XMLException.buildMessage(systemID, lineNr, e, msg, reportParams));
		this.systemID = systemID;
		this.lineNr = lineNr;
		encapsulatedException = e;
		this.msg = XMLException.buildMessage(systemID, lineNr, e, msg,
		    reportParams);
	}

	/**
	 * Creates a new exception.
	 * 
	 * @param systemID
	 *            the system ID of the XML data where the exception occurred
	 * @param lineNr
	 *            the line number in the XML data where the exception occurred.
	 * @param msg
	 *            the message of the exception.
	 */
	public XMLException(final String systemID, final int lineNr,
	                    final String msg) {
		this(systemID, lineNr, null, msg, true);
	}

	/**
	 * Cleans up the object when it's destroyed.
	 */
	@Override
	protected void finalize() throws Throwable {
		systemID = null;
		encapsulatedException = null;
		super.finalize();
	}

	/**
	 * Returns the encapsulated exception, or null if no exception is
	 * encapsulated.
	 */
	public Exception getException() {
		return encapsulatedException;
	}

	/**
	 * Returns the line number in the XML data where the exception occurred. If
	 * there is no line number known, -1 is returned.
	 */
	public int getLineNr() {
		return lineNr;
	}

	/**
	 * Returns the system ID of the XML data where the exception occurred. If
	 * there is no system ID known, null is returned.
	 */
	public String getSystemID() {
		return systemID;
	}

	/**
	 * Dumps the exception stack to System.err.
	 */
	@Override
	public void printStackTrace() {
		super.printStackTrace();
		if (encapsulatedException != null) {
			System.err.println("*** Nested Exception:");
			encapsulatedException.printStackTrace();
		}
	}

	/**
	 * Dumps the exception stack to an output stream.
	 * 
	 * @param stream
	 *            the output stream
	 */
	@Override
	public void printStackTrace(final PrintStream stream) {
		super.printStackTrace(stream);
		if (encapsulatedException != null) {
			stream.println("*** Nested Exception:");
			encapsulatedException.printStackTrace(stream);
		}
	}

	/**
	 * Dumps the exception stack to a print writer.
	 * 
	 * @param writer
	 *            the print writer
	 */
	@Override
	public void printStackTrace(final PrintWriter writer) {
		super.printStackTrace(writer);
		if (encapsulatedException != null) {
			writer.println("*** Nested Exception:");
			encapsulatedException.printStackTrace(writer);
		}
	}

	/**
	 * Returns a string representation of the exception.
	 */
	@Override
	public String toString() {
		return msg;
	}
}
