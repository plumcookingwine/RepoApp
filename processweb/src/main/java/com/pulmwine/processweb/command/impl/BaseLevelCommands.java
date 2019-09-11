package com.pulmwine.processweb.command.impl;

import android.content.Context;
import com.alibaba.android.arouter.launcher.ARouter;
import com.pulmwine.processweb.bridge.call.ResultCallback;
import com.pulmwine.processweb.command.Command;
import com.pulmwine.processweb.command.Commands;
import com.pulmwine.processweb.common.WebConstants;

import java.util.Map;

/**
 * Created by xud on 2017/12/16.
 */
public class BaseLevelCommands extends Commands {

    public BaseLevelCommands() {
        registerCommands();
    }

    @Override
    public int getCommandLevel() {
        return WebConstants.LEVEL_BASE;
    }

    private void registerCommands() {
        registerCommand(pageRouterCommand);
    }

    /**
     * 页面路由
     */
    private final Command pageRouterCommand = new Command() {

        @Override
        public String name() {
            return "router";
        }

        @Override
        public void exec(Context context, Map params, ResultCallback resultBack) {
            String newUrl = params.get("url").toString();
            ARouter.getInstance().build(newUrl).navigation();
        }
    };
}
