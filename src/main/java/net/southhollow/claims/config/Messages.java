package net.southhollow.claims.config;

import net.southhollow.claims.handler.ConfigHandler;

public class Messages extends ConfigHandler {

    private Messages(String fileName) {
        super(fileName);
    }

    public static String
            UNVALID_PLAYERNAME,
            OUTSIDE_CLAIM,
            NO_ARGUMENTS,
            BAN_SELF,
            BAN_OWNER,
            PROTECTED,
            NO_ACCESS,
            BANNED,
            BANNED_TARGET,
            ALREADY_BANNED,
            UNBANNED,
            UNBANNED_TARGET,
            NOT_BANNED,
            UNVALID_NUMBER,
            LIST_HEADER,
            LIST_EMPTY,
            TITLE_MESSAGE,
            SUBTITLE_MESSAGE, //teleport bellow
            TELEPORT_TP,
            LIST_TP_HEADER,
            LIST_NOCLAIM,
            LIST_CLAIMID,
            LIST_NAME,
            LIST_COORDS,
            LIST_CLAIMID_COLOR,
            LIST_NAME_COLOR,
            LIST_COORDS_COLOR,
            SETNAME_NOARGS,
            SETNAME_NONAME,
            SETNAME_SETNAME,
            SETSPAWN_OUTSIDE,
            SETSPAWN_NOOWNER,
            SETSPAWN_SETSPAWN,
            COOLDOWN_COOLDOWN,
            WARMUP_TITLE,
            WARMUP_SUBTITLE,
            WARMUP_CANCELLED,
            UNVALID_CLAIMID;

    private void onLoad() {

        //claim bans
        UNVALID_PLAYERNAME = getString("unvalid-playername");
        OUTSIDE_CLAIM = getString("outside-claim");
        NO_ARGUMENTS = getString("no-arguments");
        BAN_SELF = getString("ban-self");
        BAN_OWNER = getString("ban-owner");
        PROTECTED = getString("protected");
        NO_ACCESS = getString("no-access");
        BANNED = getString("banned");
        BANNED_TARGET = getString("banned-target");
        ALREADY_BANNED = getString("already-banned");
        UNBANNED = getString("unbanned");
        UNBANNED_TARGET = getString("unbanned-target");
        NOT_BANNED = getString("not-banned");
        UNVALID_NUMBER = getString("unvalid-number");
        LIST_HEADER = getString("list-header");
        LIST_EMPTY = getString("list-empty");
        TITLE_MESSAGE = getString("title-message");
        SUBTITLE_MESSAGE = getString("subtitle-message");
        //claim tp
        TELEPORT_TP = getString("teleport.teleport");
        LIST_TP_HEADER = getString("list.header");
        LIST_NOCLAIM = getString("list.no-claims");
        LIST_CLAIMID = getString("list.claimid");
        LIST_NAME = getString("list.claim-name");
        LIST_COORDS = getString("list.coordinate");
        LIST_CLAIMID_COLOR = getString("list.claimid-color");
        LIST_NAME_COLOR = getString("list.claim-name-color");
        LIST_COORDS_COLOR = getString("list.coordinate-color");
        SETNAME_NOARGS = getString("setname.no-arguments");
        SETNAME_NONAME = getString("setname.no-name");
        SETNAME_SETNAME = getString("setname.set-name");
        SETSPAWN_OUTSIDE = getString("setspawn.outside-claim");
        SETSPAWN_NOOWNER = getString("setspawn.no-owner");
        SETSPAWN_SETSPAWN = getString("setspawn.set-spawn");
        COOLDOWN_COOLDOWN = getString("cooldown.cooldown");
        WARMUP_TITLE = getString("warmup.title");
        WARMUP_SUBTITLE = getString("warmup.subtitle");
        WARMUP_CANCELLED = getString("warmup.cancelled");
        UNVALID_CLAIMID = getString("miscellaneous.unvalid-claimid");

    }

    public static void initialize() {
        new Messages("messages.yml").onLoad();
    }

    public static String placeholders(String message, String target, String source, String claimowner) {
        final String converted = message.
                replaceAll("%target%", target).
                replaceAll("%source%", source).
                replaceAll("%claimowner%", claimowner);

        return converted;

    }

}
