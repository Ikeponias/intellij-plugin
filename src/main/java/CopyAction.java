import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
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

public class CopyAction extends PsiElementBaseIntentionAction {

    private static final String NEW_LINE;
    private static final Map<String, String> initialValueLUT = new HashMap<>();

    static {
        initialValueLUT.put("boolean", "false");
        initialValueLUT.put("Boolean", "false");
        initialValueLUT.put("int", "0");
        initialValueLUT.put("byte", "(byte)0");
        initialValueLUT.put("Byte", "(byte)0");
        initialValueLUT.put("Integer", "0");
        initialValueLUT.put("String", "\"\"");
        initialValueLUT.put("BigDecimal", "new BigDecimal(\"0\")");
        initialValueLUT.put("Long", "0L");
        initialValueLUT.put("long", "0L");
        initialValueLUT.put("short", "(short)0");
        initialValueLUT.put("Short", "(short)0");
        initialValueLUT.put("Date", "new Date()");
        initialValueLUT.put("float", "0.0F");
        initialValueLUT.put("Float", "0.0F");
        initialValueLUT.put("double", "0.0D");
        initialValueLUT.put("Double", "0.0D");
        initialValueLUT.put("Character", "\'\'");
        initialValueLUT.put("char", "\'\'");
        initialValueLUT.put("LocalDateTime", "LocalDateTime.now()");
        initialValueLUT.put("LocalDate", "LocalDate.now()");
        initialValueLUT.put("List", "[]");

        NEW_LINE = System.getProperty("line.separator");
    }

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
    public String getText() { return "Add Field to ClipBoard";}

    private String makeText(final String className, final PsiField[] psiFields) {
        if(ArrayUtils.isEmpty(psiFields)) return null;

        String clipBoardText = "new " + className + "(" + NEW_LINE;

        clipBoardText += Arrays.stream(psiFields)
                                 .filter(p -> !p.hasModifierProperty(PsiModifier.STATIC))
                                 .map(p -> buildText(p))
                                 .collect(Collectors.joining("," + NEW_LINE));
        clipBoardText += ")";

        return clipBoardText;
    }

    private String buildText(@Nonnull final PsiField psiField) {
        StringBuilder sb = new StringBuilder();
        sb.append("/* " + psiField.getName() + " */ ");
        if(psiField.getType().toString().contains("List")) {
            return sb.append("[]").toString();
        };

        return sb.append(Optional.ofNullable(
                initialValueLUT.get(psiField.getTypeElement().getFirstChild().getText()))
                .orElse("new " + psiField.getType().toString().split(":")[1] + "()")).toString();
    }
}
