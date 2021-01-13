import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class CopyBuilderAction extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor,
                       @NotNull PsiElement element) throws IncorrectOperationException {
        final PsiWhiteSpaceImpl psiWhiteSpace = (PsiWhiteSpaceImpl) element;
        if (psiWhiteSpace.getParent() != null) {
            final PsiClassImpl psiClass = (PsiClassImpl) psiWhiteSpace.getParent();
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
    public String getText() { return "Copy fields to clipBoard for Builder";}

    private String makeText(final String className, final PsiField[] psiFields) {
        if(ArrayUtils.isEmpty(psiFields)) return null;

        String clipBoardText = className + ".builder()" + InitialValue.NEW_LINE;

        clipBoardText += Arrays.stream(psiFields)
                                 .filter(p -> !p.hasModifierProperty(PsiModifier.STATIC))
                                 .map(this::buildText)
                                 .collect(Collectors.joining(InitialValue.NEW_LINE));

        return clipBoardText;
    }

    private String buildText(@Nonnull final PsiField psiField) {
        StringBuilder sb = new StringBuilder();
        sb.append("/* ").append(psiField.getName()).append(" */ ");
        sb.append(".").append(psiField.getName()).append("(");

        if(psiField.getType().toString().contains("List")) {
            return sb.append("[]").toString();
        };

        sb.append(Optional.ofNullable(
                InitialValue.VALUE_LUT.get(psiField.getTypeElement().getFirstChild().getText()))
                .orElse("new " + psiField.getType().toString().split(":")[1] + "()"));

        return sb.append(")").toString();
    }
}
