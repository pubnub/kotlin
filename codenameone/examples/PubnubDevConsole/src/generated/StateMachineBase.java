/**
 * This class contains generated code from the Codename One Designer, DO NOT MODIFY!
 * This class is designed for subclassing that way the code generator can overwrite it
 * anytime without erasing your changes which should exist in a subclass!
 * For details about this file and how it works please read this blog post:
 * http://codenameone.blogspot.com/2010/10/ui-builder-class-how-to-actually-use.html
*/
package generated;

import com.codename1.ui.*;
import com.codename1.ui.util.*;
import com.codename1.ui.plaf.*;
import com.codename1.ui.events.*;

public abstract class StateMachineBase extends UIBuilder {
    private Container aboutToShowThisContainer;
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    /**
    * @deprecated use the version that accepts a resource as an argument instead

    **/
    protected void initVars() {}

    protected void initVars(Resources res) {}

    public StateMachineBase(Resources res, String resPath, boolean loadTheme) {
        startApp(res, resPath, loadTheme);
    }

    public Container startApp(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        if (loadTheme) {
            if (res == null) {
                try {
                    if (resPath.endsWith(".res")) {
                        res = Resources.open(resPath);
                        System.out.println("Warning: you should construct the state machine without the .res extension to allow theme overlays");
                    } else {
                        res = Resources.openLayered(resPath);
                    }
                } catch (java.io.IOException err) {
                    err.printStackTrace();
                }
            }
            initTheme(res);
        }
        if (res != null) {
            setResourceFilePath(resPath);
            setResourceFile(res);
            initVars(res);
            return showForm(getFirstFormName(), null);
        } else {
            Form f = (Form)createContainer(resPath, getFirstFormName());
            initVars(fetchResourceFile());
            beforeShow(f);
            f.show();
            postShow(f);
            return f;
        }
    }

    protected String getFirstFormName() {
        return "GUI 1";
    }

    public Container createWidget(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        if (loadTheme) {
            if (res == null) {
                try {
                    res = Resources.openLayered(resPath);
                } catch (java.io.IOException err) {
                    err.printStackTrace();
                }
            }
            initTheme(res);
        }
        return createContainer(resPath, "GUI 1");
    }

    protected void initTheme(Resources res) {
        String[] themes = res.getThemeResourceNames();
        if (themes != null && themes.length > 0) {
            UIManager.getInstance().setThemeProps(res.getTheme(themes[0]));
        }
    }

    public StateMachineBase() {
    }

    public StateMachineBase(String resPath) {
        this(null, resPath, true);
    }

    public StateMachineBase(Resources res) {
        this(res, null, true);
    }

    public StateMachineBase(String resPath, boolean loadTheme) {
        this(null, resPath, loadTheme);
    }

    public StateMachineBase(Resources res, boolean loadTheme) {
        this(res, null, loadTheme);
    }

    public com.codename1.ui.Button findBtnHereNowOk(Component root) {
        return (com.codename1.ui.Button)findByName("btnHereNowOk", root);
    }

    public com.codename1.ui.Button findBtnHereNowOk() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnHereNowOk", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnHereNowOk", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextArea findTxtPublishMessage(Component root) {
        return (com.codename1.ui.TextArea)findByName("txtPublishMessage", root);
    }

    public com.codename1.ui.TextArea findTxtPublishMessage() {
        com.codename1.ui.TextArea cmp = (com.codename1.ui.TextArea)findByName("txtPublishMessage", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextArea)findByName("txtPublishMessage", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextArea findTxtPublishChannel(Component root) {
        return (com.codename1.ui.TextArea)findByName("txtPublishChannel", root);
    }

    public com.codename1.ui.TextArea findTxtPublishChannel() {
        com.codename1.ui.TextArea cmp = (com.codename1.ui.TextArea)findByName("txtPublishChannel", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextArea)findByName("txtPublishChannel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findButton6(Component root) {
        return (com.codename1.ui.Button)findByName("Button6", root);
    }

    public com.codename1.ui.Button findButton6() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Button6", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Button6", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findChannel(Component root) {
        return (com.codename1.ui.Label)findByName("Channel", root);
    }

    public com.codename1.ui.Label findChannel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Channel", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Channel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findButton5(Component root) {
        return (com.codename1.ui.Button)findByName("Button5", root);
    }

    public com.codename1.ui.Button findButton5() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Button5", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Button5", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findButton4(Component root) {
        return (com.codename1.ui.Button)findByName("Button4", root);
    }

    public com.codename1.ui.Button findButton4() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Button4", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Button4", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findButton3(Component root) {
        return (com.codename1.ui.Button)findByName("Button3", root);
    }

    public com.codename1.ui.Button findButton3() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Button3", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Button3", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findButton2(Component root) {
        return (com.codename1.ui.Button)findByName("Button2", root);
    }

    public com.codename1.ui.Button findButton2() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Button2", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Button2", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnUnsubscribeOk(Component root) {
        return (com.codename1.ui.Button)findByName("btnUnsubscribeOk", root);
    }

    public com.codename1.ui.Button findBtnUnsubscribeOk() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnUnsubscribeOk", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnUnsubscribeOk", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPresenceOk(Component root) {
        return (com.codename1.ui.Button)findByName("btnPresenceOk", root);
    }

    public com.codename1.ui.Button findBtnPresenceOk() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPresenceOk", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPresenceOk", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPublish(Component root) {
        return (com.codename1.ui.Button)findByName("btnPublish", root);
    }

    public com.codename1.ui.Button findBtnPublish() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPublish", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPublish", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findButton(Component root) {
        return (com.codename1.ui.Button)findByName("Button", root);
    }

    public com.codename1.ui.Button findButton() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Button", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Button", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTxtUnsubscribePresenceChannel(Component root) {
        return (com.codename1.ui.TextField)findByName("txtUnsubscribePresenceChannel", root);
    }

    public com.codename1.ui.TextField findTxtUnsubscribePresenceChannel() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("txtUnsubscribePresenceChannel", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("txtUnsubscribePresenceChannel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel1(Component root) {
        return (com.codename1.ui.Label)findByName("Label1", root);
    }

    public com.codename1.ui.Label findLabel1() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label1", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTxtHistoryChannel(Component root) {
        return (com.codename1.ui.TextField)findByName("txtHistoryChannel", root);
    }

    public com.codename1.ui.TextField findTxtHistoryChannel() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("txtHistoryChannel", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("txtHistoryChannel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnDar(Component root) {
        return (com.codename1.ui.Button)findByName("btnDar", root);
    }

    public com.codename1.ui.Button findBtnDar() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnDar", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnDar", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTxtPresenceChannel(Component root) {
        return (com.codename1.ui.TextField)findByName("txtPresenceChannel", root);
    }

    public com.codename1.ui.TextField findTxtPresenceChannel() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("txtPresenceChannel", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("txtPresenceChannel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnUnsubscribePresenceOk(Component root) {
        return (com.codename1.ui.Button)findByName("btnUnsubscribePresenceOk", root);
    }

    public com.codename1.ui.Button findBtnUnsubscribePresenceOk() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnUnsubscribePresenceOk", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnUnsubscribePresenceOk", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnPublishOk(Component root) {
        return (com.codename1.ui.Button)findByName("btnPublishOk", root);
    }

    public com.codename1.ui.Button findBtnPublishOk() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnPublishOk", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnPublishOk", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnHistoryOk(Component root) {
        return (com.codename1.ui.Button)findByName("btnHistoryOk", root);
    }

    public com.codename1.ui.Button findBtnHistoryOk() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnHistoryOk", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnHistoryOk", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel(Component root) {
        return (com.codename1.ui.Label)findByName("label", root);
    }

    public com.codename1.ui.Label findLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("label", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("label", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTxtUnsubscribeChannel(Component root) {
        return (com.codename1.ui.TextField)findByName("txtUnsubscribeChannel", root);
    }

    public com.codename1.ui.TextField findTxtUnsubscribeChannel() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("txtUnsubscribeChannel", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("txtUnsubscribeChannel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findTxtSubscribeChannel(Component root) {
        return (com.codename1.ui.TextField)findByName("txtSubscribeChannel", root);
    }

    public com.codename1.ui.TextField findTxtSubscribeChannel() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("txtSubscribeChannel", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("txtSubscribeChannel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextArea findTxtHereNowChannel(Component root) {
        return (com.codename1.ui.TextArea)findByName("txtHereNowChannel", root);
    }

    public com.codename1.ui.TextArea findTxtHereNowChannel() {
        com.codename1.ui.TextArea cmp = (com.codename1.ui.TextArea)findByName("txtHereNowChannel", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextArea)findByName("txtHereNowChannel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findBtnTime(Component root) {
        return (com.codename1.ui.Button)findByName("btnTime", root);
    }

    public com.codename1.ui.Button findBtnTime() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("btnTime", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("btnTime", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findTxtSubscribeOk(Component root) {
        return (com.codename1.ui.Button)findByName("txtSubscribeOk", root);
    }

    public com.codename1.ui.Button findTxtSubscribeOk() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("txtSubscribeOk", Display.getInstance().getCurrent());
        if (cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("txtSubscribeOk", aboutToShowThisContainer);
        }
        return cmp;
    }

    public static final int COMMAND_GUI1HereNow = 5;
    public static final int COMMAND_SubscribeSubscribe = 9;
    public static final int COMMAND_GUI1Unsubscribe = 6;
    public static final int COMMAND_GUI1History = 4;
    public static final int COMMAND_GUI1Subscribe = 2;
    public static final int COMMAND_UnsubscribePresenceUnsubscribeFromPresenceEvents = 11;
    public static final int COMMAND_GUI1UnsubscribePresence = 7;
    public static final int COMMAND_GUI1Time = 8;
    public static final int COMMAND_GUI1Presence = 3;
    public static final int COMMAND_GUI1Publish = 1;
    public static final int COMMAND_HereNowGetHereNowData = 10;

    protected boolean onGUI1HereNow() {
        return false;
    }

    protected boolean onSubscribeSubscribe() {
        return false;
    }

    protected boolean onGUI1Unsubscribe() {
        return false;
    }

    protected boolean onGUI1History() {
        return false;
    }

    protected boolean onGUI1Subscribe() {
        return false;
    }

    protected boolean onUnsubscribePresenceUnsubscribeFromPresenceEvents() {
        return false;
    }

    protected boolean onGUI1UnsubscribePresence() {
        return false;
    }

    protected boolean onGUI1Time() {
        return false;
    }

    protected boolean onGUI1Presence() {
        return false;
    }

    protected boolean onGUI1Publish() {
        return false;
    }

    protected boolean onHereNowGetHereNowData() {
        return false;
    }

    protected void processCommand(ActionEvent ev, Command cmd) {
        switch (cmd.getId()) {
        case COMMAND_GUI1HereNow:
            if (onGUI1HereNow()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_SubscribeSubscribe:
            if (onSubscribeSubscribe()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_GUI1Unsubscribe:
            if (onGUI1Unsubscribe()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_GUI1History:
            if (onGUI1History()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_GUI1Subscribe:
            if (onGUI1Subscribe()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_UnsubscribePresenceUnsubscribeFromPresenceEvents:
            if (onUnsubscribePresenceUnsubscribeFromPresenceEvents()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_GUI1UnsubscribePresence:
            if (onGUI1UnsubscribePresence()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_GUI1Time:
            if (onGUI1Time()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_GUI1Presence:
            if (onGUI1Presence()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_GUI1Publish:
            if (onGUI1Publish()) {
                ev.consume();
                return;
            }
            break;

        case COMMAND_HereNowGetHereNowData:
            if (onHereNowGetHereNowData()) {
                ev.consume();
                return;
            }
            break;

        }
        if (ev.getComponent() != null) {
            handleComponentAction(ev.getComponent(), ev);
        }
    }

    protected void exitForm(Form f) {
        if ("Presence".equals(f.getName())) {
            exitPresence(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("GUI 1".equals(f.getName())) {
            exitGUI1(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Unsubscribe".equals(f.getName())) {
            exitUnsubscribe(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("History".equals(f.getName())) {
            exitHistory(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Subscribe".equals(f.getName())) {
            exitSubscribe(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("UnsubscribePresence".equals(f.getName())) {
            exitUnsubscribePresence(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Here Now".equals(f.getName())) {
            exitHereNow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Publish".equals(f.getName())) {
            exitPublish(f);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void exitPresence(Form f) {
    }


    protected void exitGUI1(Form f) {
    }


    protected void exitUnsubscribe(Form f) {
    }


    protected void exitHistory(Form f) {
    }


    protected void exitSubscribe(Form f) {
    }


    protected void exitUnsubscribePresence(Form f) {
    }


    protected void exitHereNow(Form f) {
    }


    protected void exitPublish(Form f) {
    }

    protected void beforeShow(Form f) {
        aboutToShowThisContainer = f;
        if ("Presence".equals(f.getName())) {
            beforePresence(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("GUI 1".equals(f.getName())) {
            beforeGUI1(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Unsubscribe".equals(f.getName())) {
            beforeUnsubscribe(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("History".equals(f.getName())) {
            beforeHistory(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Subscribe".equals(f.getName())) {
            beforeSubscribe(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("UnsubscribePresence".equals(f.getName())) {
            beforeUnsubscribePresence(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Here Now".equals(f.getName())) {
            beforeHereNow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Publish".equals(f.getName())) {
            beforePublish(f);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void beforePresence(Form f) {
    }


    protected void beforeGUI1(Form f) {
    }


    protected void beforeUnsubscribe(Form f) {
    }


    protected void beforeHistory(Form f) {
    }


    protected void beforeSubscribe(Form f) {
    }


    protected void beforeUnsubscribePresence(Form f) {
    }


    protected void beforeHereNow(Form f) {
    }


    protected void beforePublish(Form f) {
    }

    protected void beforeShowContainer(Container c) {
        aboutToShowThisContainer = c;
        if ("Presence".equals(c.getName())) {
            beforeContainerPresence(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("GUI 1".equals(c.getName())) {
            beforeContainerGUI1(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Unsubscribe".equals(c.getName())) {
            beforeContainerUnsubscribe(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("History".equals(c.getName())) {
            beforeContainerHistory(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Subscribe".equals(c.getName())) {
            beforeContainerSubscribe(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("UnsubscribePresence".equals(c.getName())) {
            beforeContainerUnsubscribePresence(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Here Now".equals(c.getName())) {
            beforeContainerHereNow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Publish".equals(c.getName())) {
            beforeContainerPublish(c);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void beforeContainerPresence(Container c) {
    }


    protected void beforeContainerGUI1(Container c) {
    }


    protected void beforeContainerUnsubscribe(Container c) {
    }


    protected void beforeContainerHistory(Container c) {
    }


    protected void beforeContainerSubscribe(Container c) {
    }


    protected void beforeContainerUnsubscribePresence(Container c) {
    }


    protected void beforeContainerHereNow(Container c) {
    }


    protected void beforeContainerPublish(Container c) {
    }

    protected void postShow(Form f) {
        if ("Presence".equals(f.getName())) {
            postPresence(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("GUI 1".equals(f.getName())) {
            postGUI1(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Unsubscribe".equals(f.getName())) {
            postUnsubscribe(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("History".equals(f.getName())) {
            postHistory(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Subscribe".equals(f.getName())) {
            postSubscribe(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("UnsubscribePresence".equals(f.getName())) {
            postUnsubscribePresence(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Here Now".equals(f.getName())) {
            postHereNow(f);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Publish".equals(f.getName())) {
            postPublish(f);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void postPresence(Form f) {
    }


    protected void postGUI1(Form f) {
    }


    protected void postUnsubscribe(Form f) {
    }


    protected void postHistory(Form f) {
    }


    protected void postSubscribe(Form f) {
    }


    protected void postUnsubscribePresence(Form f) {
    }


    protected void postHereNow(Form f) {
    }


    protected void postPublish(Form f) {
    }

    protected void postShowContainer(Container c) {
        if ("Presence".equals(c.getName())) {
            postContainerPresence(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("GUI 1".equals(c.getName())) {
            postContainerGUI1(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Unsubscribe".equals(c.getName())) {
            postContainerUnsubscribe(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("History".equals(c.getName())) {
            postContainerHistory(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Subscribe".equals(c.getName())) {
            postContainerSubscribe(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("UnsubscribePresence".equals(c.getName())) {
            postContainerUnsubscribePresence(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Here Now".equals(c.getName())) {
            postContainerHereNow(c);
            aboutToShowThisContainer = null;
            return;
        }

        if ("Publish".equals(c.getName())) {
            postContainerPublish(c);
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void postContainerPresence(Container c) {
    }


    protected void postContainerGUI1(Container c) {
    }


    protected void postContainerUnsubscribe(Container c) {
    }


    protected void postContainerHistory(Container c) {
    }


    protected void postContainerSubscribe(Container c) {
    }


    protected void postContainerUnsubscribePresence(Container c) {
    }


    protected void postContainerHereNow(Container c) {
    }


    protected void postContainerPublish(Container c) {
    }

    protected void onCreateRoot(String rootName) {
        if ("Presence".equals(rootName)) {
            onCreatePresence();
            aboutToShowThisContainer = null;
            return;
        }

        if ("GUI 1".equals(rootName)) {
            onCreateGUI1();
            aboutToShowThisContainer = null;
            return;
        }

        if ("Unsubscribe".equals(rootName)) {
            onCreateUnsubscribe();
            aboutToShowThisContainer = null;
            return;
        }

        if ("History".equals(rootName)) {
            onCreateHistory();
            aboutToShowThisContainer = null;
            return;
        }

        if ("Subscribe".equals(rootName)) {
            onCreateSubscribe();
            aboutToShowThisContainer = null;
            return;
        }

        if ("UnsubscribePresence".equals(rootName)) {
            onCreateUnsubscribePresence();
            aboutToShowThisContainer = null;
            return;
        }

        if ("Here Now".equals(rootName)) {
            onCreateHereNow();
            aboutToShowThisContainer = null;
            return;
        }

        if ("Publish".equals(rootName)) {
            onCreatePublish();
            aboutToShowThisContainer = null;
            return;
        }

    }


    protected void onCreatePresence() {
    }


    protected void onCreateGUI1() {
    }


    protected void onCreateUnsubscribe() {
    }


    protected void onCreateHistory() {
    }


    protected void onCreateSubscribe() {
    }


    protected void onCreateUnsubscribePresence() {
    }


    protected void onCreateHereNow() {
    }


    protected void onCreatePublish() {
    }

    protected void handleComponentAction(Component c, ActionEvent event) {
        Container rootContainerAncestor = getRootAncestor(c);
        if (rootContainerAncestor == null) return;
        String rootContainerName = rootContainerAncestor.getName();
        if (c.getParent().getLeadParent() != null) {
            c = c.getParent().getLeadParent();
        }
        if (rootContainerName == null) return;
        if (rootContainerName.equals("Presence")) {
            if ("txtPresenceChannel".equals(c.getName())) {
                onPresence_TxtPresenceChannelAction(c, event);
                return;
            }
            if ("btnPresenceOk".equals(c.getName())) {
                onPresence_BtnPresenceOkAction(c, event);
                return;
            }
        }
        if (rootContainerName.equals("GUI 1")) {
            if ("Button".equals(c.getName())) {
                onGUI1_ButtonAction(c, event);
                return;
            }
            if ("btnPublish".equals(c.getName())) {
                onGUI1_BtnPublishAction(c, event);
                return;
            }
            if ("Button2".equals(c.getName())) {
                onGUI1_Button2Action(c, event);
                return;
            }
            if ("Button3".equals(c.getName())) {
                onGUI1_Button3Action(c, event);
                return;
            }
            if ("Button4".equals(c.getName())) {
                onGUI1_Button4Action(c, event);
                return;
            }
            if ("Button5".equals(c.getName())) {
                onGUI1_Button5Action(c, event);
                return;
            }
            if ("Button6".equals(c.getName())) {
                onGUI1_Button6Action(c, event);
                return;
            }
            if ("btnTime".equals(c.getName())) {
                onGUI1_BtnTimeAction(c, event);
                return;
            }
            if ("btnDar".equals(c.getName())) {
                onGUI1_BtnDarAction(c, event);
                return;
            }
        }
        if (rootContainerName.equals("Unsubscribe")) {
            if ("txtUnsubscribeChannel".equals(c.getName())) {
                onUnsubscribe_TxtUnsubscribeChannelAction(c, event);
                return;
            }
            if ("btnUnsubscribeOk".equals(c.getName())) {
                onUnsubscribe_BtnUnsubscribeOkAction(c, event);
                return;
            }
        }
        if (rootContainerName.equals("History")) {
            if ("txtHistoryChannel".equals(c.getName())) {
                onHistory_TxtHistoryChannelAction(c, event);
                return;
            }
            if ("btnHistoryOk".equals(c.getName())) {
                onHistory_BtnHistoryOkAction(c, event);
                return;
            }
        }
        if (rootContainerName.equals("Subscribe")) {
            if ("txtSubscribeChannel".equals(c.getName())) {
                onSubscribe_TxtSubscribeChannelAction(c, event);
                return;
            }
            if ("txtSubscribeOk".equals(c.getName())) {
                onSubscribe_TxtSubscribeOkAction(c, event);
                return;
            }
        }
        if (rootContainerName.equals("UnsubscribePresence")) {
            if ("txtUnsubscribePresenceChannel".equals(c.getName())) {
                onUnsubscribePresence_TxtUnsubscribePresenceChannelAction(c, event);
                return;
            }
            if ("btnUnsubscribePresenceOk".equals(c.getName())) {
                onUnsubscribePresence_BtnUnsubscribePresenceOkAction(c, event);
                return;
            }
        }
        if (rootContainerName.equals("Here Now")) {
            if ("txtHereNowChannel".equals(c.getName())) {
                onHereNow_TxtHereNowChannelAction(c, event);
                return;
            }
            if ("btnHereNowOk".equals(c.getName())) {
                onHereNow_BtnHereNowOkAction(c, event);
                return;
            }
        }
        if (rootContainerName.equals("Publish")) {
            if ("txtPublishChannel".equals(c.getName())) {
                onPublish_TxtPublishChannelAction(c, event);
                return;
            }
            if ("txtPublishMessage".equals(c.getName())) {
                onPublish_TxtPublishMessageAction(c, event);
                return;
            }
            if ("btnPublishOk".equals(c.getName())) {
                onPublish_BtnPublishOkAction(c, event);
                return;
            }
        }
    }

    protected void onPresence_TxtPresenceChannelAction(Component c, ActionEvent event) {
    }

    protected void onPresence_BtnPresenceOkAction(Component c, ActionEvent event) {
    }

    protected void onGUI1_ButtonAction(Component c, ActionEvent event) {
    }

    protected void onGUI1_BtnPublishAction(Component c, ActionEvent event) {
    }

    protected void onGUI1_Button2Action(Component c, ActionEvent event) {
    }

    protected void onGUI1_Button3Action(Component c, ActionEvent event) {
    }

    protected void onGUI1_Button4Action(Component c, ActionEvent event) {
    }

    protected void onGUI1_Button5Action(Component c, ActionEvent event) {
    }

    protected void onGUI1_Button6Action(Component c, ActionEvent event) {
    }

    protected void onGUI1_BtnTimeAction(Component c, ActionEvent event) {
    }

    protected void onGUI1_BtnDarAction(Component c, ActionEvent event) {
    }

    protected void onUnsubscribe_TxtUnsubscribeChannelAction(Component c, ActionEvent event) {
    }

    protected void onUnsubscribe_BtnUnsubscribeOkAction(Component c, ActionEvent event) {
    }

    protected void onHistory_TxtHistoryChannelAction(Component c, ActionEvent event) {
    }

    protected void onHistory_BtnHistoryOkAction(Component c, ActionEvent event) {
    }

    protected void onSubscribe_TxtSubscribeChannelAction(Component c, ActionEvent event) {
    }

    protected void onSubscribe_TxtSubscribeOkAction(Component c, ActionEvent event) {
    }

    protected void onUnsubscribePresence_TxtUnsubscribePresenceChannelAction(Component c, ActionEvent event) {
    }

    protected void onUnsubscribePresence_BtnUnsubscribePresenceOkAction(Component c, ActionEvent event) {
    }

    protected void onHereNow_TxtHereNowChannelAction(Component c, ActionEvent event) {
    }

    protected void onHereNow_BtnHereNowOkAction(Component c, ActionEvent event) {
    }

    protected void onPublish_TxtPublishChannelAction(Component c, ActionEvent event) {
    }

    protected void onPublish_TxtPublishMessageAction(Component c, ActionEvent event) {
    }

    protected void onPublish_BtnPublishOkAction(Component c, ActionEvent event) {
    }

}
