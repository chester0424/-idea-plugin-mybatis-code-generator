package zone.pusu.mybatisCodeGenerator.tool;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocToken;

import javax.annotation.Nullable;

/**
 *
 */
public class PsiUtil {
    /**
     * 获取注释文本
     *
     * @param docComment
     * @return
     */
    public static @Nullable String getCommentText(@Nullable PsiDocComment docComment) {
        if (docComment != null) {
            PsiElement[] psiElements = docComment.getDescriptionElements();
            for (PsiElement psiElement : psiElements) {
                if (psiElement instanceof PsiDocToken) {
                    String comment = psiElement.getText();
                    if (!StringUtil.isNullOrEmpty(comment)) {
                        return StringUtil.trim(comment);
                    }
                }
            }
        }
        return null;
    }
}
