package com.rcp.eclipse.plugin.manglecase;

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

/*
 
The action enablement code has also been modified to support non
IStructuredSelections.  According to one dictionary, a selection is "one
that is selected" or "a collection of selected things".  In reflection of
this definition, the workbench will treat an IStructuredSelection as a
collection of selected things, and any other selection type as one thing.

For instance, suppose you add an action to the window (via an action set),
and it should be enabled if some text is selected in an editor.  You can
use the following declaration to enable the action.

        <action id="org.eclipse.ui.tests.internal.as_1"
            label="anyText"
            menubarPath
="org.eclipse.ui.tests.internal.TextSelectionMenu/group1"
            class="org.eclipse.ui.tests.api.MockActionDelegate"
          enablesFor="1">
            <selection class="org.eclipse.jface.text.ITextSelection"/>
        </action>

If you want to add an action to the window which is enabled when an IFile
with extension .txt is selected, you can use the following declaration
(shown with the new enablement block).  In this case, the IFile will be
exposed in an IStructuredSelection, but there is no need to mention the
IStructuredSelection in the action declaration.  The workbench treats
IStructuredSelection as a container for the important data inside.

        <action id="org.eclipse.ui.tests.internal.as_2"
            label="anyTextFile"
            menubarPath
="org.eclipse.ui.tests.internal.TextSelectionMenu/group1"
            class="org.eclipse.ui.tests.api.MockActionDelegate">
            <enablement>
               <objectClass name="org.eclipse.core.IFile"/>
               <objectState name="extension" value="txt"/>
            </enablement>
        </action>
*/
public class ToLowerCaseActionDelegateBackup extends ActionDelegate implements
		IEditorActionDelegate, IWorkbenchWindowActionDelegate {

	private IEditorPart editorPart;
	private IWorkbenchWindow workbenchWindow;

	/**
	 * @see ActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		if (workbenchWindow != null) {
//			System.out.println("workbenchWindow=" + workbenchWindow.getClass()
//					+ " ww=" + workbenchWindow);
			IEditorPart editor2 = workbenchWindow.getActivePage()
					.getActiveEditor();
//			System.out.println("editor2=" + editor2.getClass());
			ITextEditor textEditor;
			if (editor2 instanceof ITextEditor) {
				textEditor = (ITextEditor) editor2;
			} else {
				workbenchWindow.getShell().getDisplay().beep();
				return;
			}
			ITextSelection sel = ((ITextSelection) textEditor
					.getSelectionProvider().getSelection());
			if (textEditor != null && sel != null) {
				convertToLowerCase(sel, textEditor);
				return;
			}
		}
		if (editorPart instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor) editorPart;
			ITextSelection selection = (ITextSelection) editor
					.getSelectionProvider().getSelection();

			convertToLowerCase(selection, editor);
		}
	}

	private void convertToLowerCase(final ITextSelection selection, final ITextEditor editor) {
		System.out.println("selection=" + selection.getText() + " offset="
				+ selection.getOffset() + " length=" + selection.getLength());

		IDocumentProvider provider = editor.getDocumentProvider();
		IDocument document = provider.getDocument(editor.getEditorInput());
		try {
			document.replace(selection.getOffset(), selection.getLength(),
					selection.getText().toLowerCase());
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

	public void init(IWorkbenchWindow window) {
		workbenchWindow = window;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.ActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
//	public void selectionChanged(IAction action, ISelection iselection) {
//		if (iselection instanceof ITextSelection) {
//			ITextSelection selection = (ITextSelection) iselection;
//			System.out.println("selection=" + selection.getText() + " offset="
//					+ selection.getOffset() + " length="
//					+ selection.getLength());
//		}
//		super.selectionChanged(action, iselection);
//	}
}
