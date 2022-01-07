package com.blackmc.blackbedwars.hooks.skript.classes;

import com.blackmc.blackbedwars.core.Team;

import org.jetbrains.annotations.Nullable;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;

public class TeamClassInfo {
    
    static {
        Classes.registerClass(new ClassInfo<>(Team.class, "team")
            .user("teams?")
            .name("Team")
            .description("represents a bedwars team")
            .examples("loop players of player's team:")
            .defaultExpression(new EventValueExpression<>(Team.class))
            .parser(new Parser<Team>() {

                @Override
                @Nullable
                public Team parse(String s, ParseContext context) {
                    return null;
                }

                @Override
                public boolean canParse(ParseContext context) {
                    return false;
                }

                @Override
                public String getVariableNamePattern() {
                    return null;
                }

                @Override
                public String toVariableNameString(Team team) {
                    return team.getType().name().toLowerCase() + "." + team.getSpawn().getWorld().getName().toLowerCase();
                }

                @Override
                public String toString(Team team, int flags) {
                    return toVariableNameString(team);
                }
                
            })
        );
    }

}
