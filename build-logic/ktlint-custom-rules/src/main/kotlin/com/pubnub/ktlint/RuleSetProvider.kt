/**
 * Copyright 2019 Pinterest, Inc.
 * Copyright 2016-2019 Stanley Shyiko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.pubnub.ktlint

import com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3
import com.pinterest.ktlint.rule.engine.core.api.ElementType.BINARY_EXPRESSION
import com.pinterest.ktlint.rule.engine.core.api.ElementType.BLOCK
import com.pinterest.ktlint.rule.engine.core.api.ElementType.DOT_QUALIFIED_EXPRESSION
import com.pinterest.ktlint.rule.engine.core.api.ElementType.ELSE
import com.pinterest.ktlint.rule.engine.core.api.ElementType.ELSE_KEYWORD
import com.pinterest.ktlint.rule.engine.core.api.ElementType.IF
import com.pinterest.ktlint.rule.engine.core.api.ElementType.LBRACE
import com.pinterest.ktlint.rule.engine.core.api.ElementType.RBRACE
import com.pinterest.ktlint.rule.engine.core.api.ElementType.RPAR
import com.pinterest.ktlint.rule.engine.core.api.ElementType.THEN
import com.pinterest.ktlint.rule.engine.core.api.IndentConfig
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.rule.engine.core.api.RuleSetId
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfig
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.EditorConfigProperty
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.INDENT_SIZE_PROPERTY
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.INDENT_STYLE_PROPERTY
import com.pinterest.ktlint.rule.engine.core.api.indent
import com.pinterest.ktlint.rule.engine.core.api.isPartOfComment
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpace
import com.pinterest.ktlint.rule.engine.core.api.isWhiteSpaceWithoutNewline
import com.pinterest.ktlint.rule.engine.core.api.nextSibling
import com.pinterest.ktlint.rule.engine.core.api.upsertWhitespaceBeforeMe
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.psiUtil.leaves

val ruleSetId = RuleSetId("pubnub")

class RuleSetProvider : RuleSetProviderV3(ruleSetId) {
    override fun getRuleProviders(): Set<RuleProvider> {
        return setOf(
            RuleProvider { MultiLineIfElseRule() }
        )
    }
}


public open class StandardRule internal constructor(
    id: String,
    override val visitorModifiers: Set<VisitorModifier> = emptySet(),
    override val usesEditorConfigProperties: Set<EditorConfigProperty<*>> = emptySet(),
) : Rule(
    ruleId = RuleId("${ruleSetId.value}:$id"),
    visitorModifiers = visitorModifiers,
    usesEditorConfigProperties = usesEditorConfigProperties,
    about = About(
        maintainer = "PubNub"
    ),
)

public class MultiLineIfElseRule :
    StandardRule(
        id = "multiline-if-else-required",
        usesEditorConfigProperties =
        setOf(
            INDENT_SIZE_PROPERTY,
            INDENT_STYLE_PROPERTY,
        ),
    ) {
    private var indentConfig = IndentConfig.DEFAULT_INDENT_CONFIG

    override fun beforeFirstNode(editorConfig: EditorConfig) {
        indentConfig =
            IndentConfig(
                indentStyle = editorConfig[INDENT_STYLE_PROPERTY],
                tabWidth = editorConfig[INDENT_SIZE_PROPERTY],
            )
    }

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if (node.elementType != THEN && node.elementType != ELSE) {
            return
        }

        // Ignore when already wrapped in a block
        if (node.firstChildNode?.elementType == BLOCK) {
            return
        }

        if (node.elementType == ELSE &&
            node.firstChildNode?.elementType == BINARY_EXPRESSION &&
            node.firstChildNode.firstChildNode?.elementType == IF
        ) {
            // Allow
            // val foo2 = if (bar1) {
            //     "bar1"
            // } else if (bar2) {
            //     null
            // } else {
            //     null
            // } ?: "something-else"
            return
        }

        if (node.elementType == ELSE &&
            node.firstChildNode?.elementType == DOT_QUALIFIED_EXPRESSION &&
            node.firstChildNode.firstChildNode?.elementType == IF
        ) {
            // Allow
            // val foo = if (bar1) {
            //     "bar1"
            // } else if (bar2) {
            //     "bar2"
            // } else {
            //     "bar3"
            // }.plus("foo")
            return
        }

        if (!node.treePrev.textContains('\n')) {
            if (node.firstChildNode.elementType == IF) {
                // Allow single line for:
                // else if (...)
                return
            }
            if (!node.treeParent.textContains('\n')) {
                // Don't allow single line if statements as long as they are really simple (e.g. do not contain newlines)
                //    if (...) <statement> // no else statement
                //    if (...) <statement> else <statement>
                if (node.treeParent.treeParent.elementType == ELSE) {
                    // Not even in case nested if-else-if on single line
                    //    if (...) <statement> else if (..) <statement>
                } else {
                    // don't allow any of the above
                }
            }
        }

        emit(node.firstChildNode.startOffset, "Missing { ... }", true)
        if (autoCorrect) {
            autocorrect(node)
        }
    }

    private fun autocorrect(node: ASTNode) {
        val prevLeaves =
            node
                .leaves(forward = false)
                .takeWhile { it.elementType !in listOf(RPAR, ELSE_KEYWORD) }
                .toList()
                .reversed()
        val nextLeaves =
            node
                .leaves(forward = true)
                .takeWhile { it.isWhiteSpaceWithoutNewline() || it.isPartOfComment() }
                .toList()
                .dropLastWhile { it.isWhiteSpaceWithoutNewline() }

        prevLeaves
            .firstOrNull()
            .takeIf { it.isWhiteSpace() }
            ?.let {
                (it as LeafPsiElement).rawReplaceWithText(" ")
            }
        KtBlockExpression(null).apply {
            val previousChild = node.firstChildNode
            node.replaceChild(node.firstChildNode, this)
            addChild(LeafPsiElement(LBRACE, "{"))
            addChild(PsiWhiteSpaceImpl(indentConfig.childIndentOf(node)))
            prevLeaves
                .dropWhile { it.isWhiteSpace() }
                .forEach(::addChild)
            addChild(previousChild)
            nextLeaves.forEach(::addChild)
            addChild(PsiWhiteSpaceImpl(node.indent()))
            addChild(LeafPsiElement(RBRACE, "}"))
        }

        // Make sure else starts on same line as newly inserted right brace
        if (node.elementType == THEN) {
            node
                .nextSibling { !it.isPartOfComment() }
                ?.upsertWhitespaceBeforeMe(" ")
        }
    }
}
