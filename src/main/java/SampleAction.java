import com.intellij.analysis.AnalysisScope;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.ide.macro.ClipboardContentMacro;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SampleAction extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor,
                       @NotNull PsiElement element) throws IncorrectOperationException {
        if(element.getParent() instanceof PsiClass) {
            PsiField[] psiFields = ((PsiClass) element.getParent()).getAllFields();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(psiFields[0].getType() + ":" + psiFields[0].getName());
            clipboard.setContents(selection, selection);
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor,
                               @NotNull PsiElement element) {
        return true;
    }


    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "hoge hoge";
    }

}
