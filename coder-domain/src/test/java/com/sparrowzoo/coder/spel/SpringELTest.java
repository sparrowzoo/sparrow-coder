package com.sparrowzoo.coder.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringELTest {
    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("name", "Alice");
        context.setVariable("age", 25);


        System.out.println(parser.parseExpression("'hello:' + #name").getValue(context));
        String falseString = parser.parseExpression(
                "(#age > 20) ? #name :'Unknown'").getValue(String.class);
        System.out.println(falseString);
    }
}
