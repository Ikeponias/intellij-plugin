import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class CopyAction extends PsiElementBaseIntentionAction {

    private static Map<String, String> initialValueLUT = new HashMap<>();

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
    public String getText() { return "Add Field to ClipBoard";}

    private String makeText(final String className, final PsiField[] psiFields) {
        String clipBoardText = "new " + className + "(";
        clipBoardText += Arrays.stream(psiFields)
                                 .map(p -> initialValueLUT
                                                   .get(p.getType().toString().split(":")[1]))
                                 .collect(
                                         Collectors.joining(", "));
        clipBoardText += ")";
        return clipBoardText;
    }

}
