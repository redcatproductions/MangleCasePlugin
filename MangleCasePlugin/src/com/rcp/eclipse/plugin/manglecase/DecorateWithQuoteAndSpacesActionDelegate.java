package com.rcp.eclipse.plugin.manglecase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class DecorateWithQuoteAndSpacesActionDelegate extends ActionDelegate implements
		IEditorActionDelegate, IWorkbenchWindowActionDelegate
{

	private IEditorPart editorPart;
	private IWorkbenchWindow workbenchWindow;

	/**
	 * @see ActionDelegate#run(IAction)
	 */
	public void run(IAction action)
	{
		if (workbenchWindow != null && workbenchWindow.getActivePage() != null)
		{
			editorPart = workbenchWindow.getActivePage().getActiveEditor();
		}
		if (editorPart instanceof ITextEditor)
		{
			ITextEditor editor = (ITextEditor) editorPart;
			ITextSelection selection = (ITextSelection) editor
					.getSelectionProvider()
					.getSelection();

			addQuotesAndSpaces(selection, editor);
		}
	}

	private void addQuotesAndSpaces(	final ITextSelection selection,
													final ITextEditor editor)
	{
		String text = selection.getText();
		String line = null;
		StringBuffer buffer = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(new StringReader(text));
		do
		{
			try
			{
				line = StringUtils.trim(bufferedReader.readLine());

				if (StringUtils.isNotBlank(line))
				{
					if (buffer.length() != 0)
					{
						buffer.append(" +\n");
					}
					buffer.append("\t\" ");
					buffer.append(line);
					buffer.append(" \"");
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		while (line != null);
		buffer.append(";\n");
		try
		{
			bufferedReader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		IDocumentProvider provider = editor.getDocumentProvider();
		IDocument document = provider.getDocument(editor.getEditorInput());
		try
		{
			document.replace(selection.getOffset(), selection.getLength(), buffer
					.toString());
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @see IEditorActionDelegate#setActiveEditor(IAction, IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart _targetEditor)
	{
		editorPart = _targetEditor;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window)
	{
		workbenchWindow = window;
	}
}
