import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MakeBuilderAction extends PsiElementBaseIntentionAction {

    private static final String NEW_LINE;

    static {

        NEW_LINE = System.getProperty("line.separator");
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor,
                       @NotNull PsiElement element) throws IncorrectOperationException {
        if (element.getParent() instanceof PsiClass) {
            final PsiClass psiClass = ((PsiClass) element.getParent());
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            final String clipBoardText = makeText(psiClass.getName(), psiClass.getAllFields());
            final StringSelection selection = new StringSelection(clipBoardText);
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
        return "";
    }

    @NotNull
    @Override
    public String getText() { return "Builder pattern copy to ClipBoard";}

    private String makeText(final String className, final PsiField[] psiFields) {

        String clipBoardText = className + ".builder()" + NEW_LINE;
        clipBoardText += Arrays.stream(psiFields).map(p -> "." + p.getName() + "(" + ")" + NEW_LINE);
        clipBoardText += ".build()";

        return clipBoardText;
    }
}
