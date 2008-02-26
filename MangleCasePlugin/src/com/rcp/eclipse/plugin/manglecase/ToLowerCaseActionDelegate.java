package com.rcp.eclipse.plugin.manglecase;

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

public class ToLowerCaseActionDelegate extends ActionDelegate implements
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

			convertToLowerCase(selection, editor);
		}
	}

	private void convertToLowerCase(final ITextSelection selection,
			final ITextEditor editor) {
		IDocumentProvider provider = editor.getDocumentProvider();
		IDocument document = provider.getDocument(editor.getEditorInput());
		try {
			document.replace(selection.getOffset(), selection.getLength(),
					StringUtils.lowerCase(selection.getText()));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
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
