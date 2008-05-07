package com.rcp.eclipse.plugin.manglecase;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class StripQuoteActionDelegate extends ActionDelegate implements
		IEditorActionDelegate, IWorkbenchWindowActionDelegate {

	private IEditorPart editorPart;
	private IWorkbenchWindow workbenchWindow;

	/**
	 * @see ActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		if (workbenchWindow != null && workbenchWindow.getActivePage() != null) {
			editorPart = workbenchWindow.getActivePage().getActiveEditor();
		}
		if (editorPart instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor) editorPart;
			ITextSelection selection = (ITextSelection) editor
					.getSelectionProvider().getSelection();

			stripQuotes(selection, editor);
		}
	}

	private void stripQuotes(final ITextSelection selection,
			final ITextEditor editor) {
		IDocumentProvider provider = editor.getDocumentProvider();
		Pattern pattern = Pattern
				.compile("^\\s*\\+?\\s*\"([^\"\\+]*)\"\\s*\\+?\\s*$");

		String text = selection.getText();
		String line = null;
		StringBuffer buffer = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(new StringReader(
				text));
		do {
			try {
				line = StringUtils.trim(bufferedReader.readLine());
				if (line != null) {
					Matcher matcher = pattern.matcher(line);
					if (matcher.find()) {
						System.out.println(">" + matcher.group(1) + "<");
						buffer.append(matcher.group(1));
						buffer.append("\n");
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (line != null);
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transferable = new StringSelection(buffer.toString());
		clipboard.setContents(transferable, null);
	}

	/**
	 * @see IEditorActionDelegate#setActiveEditor(IAction, IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart _targetEditor) {
		editorPart = _targetEditor;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		workbenchWindow = window;
	}
}
