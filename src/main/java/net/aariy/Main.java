package net.aariy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;

public class Main extends ListenerAdapter
{
    public static File FILE;
    public static ObjectNode node;
    public static void main(String[] args) throws IOException
    {
        if (new File("data.json").exists()) FILE = new File("data.json");
        else FILE = new File("build/resources/main/data.json");
        node = new ObjectMapper().readTree(FILE).deepCopy();
        JDA jda = JDABuilder.createDefault(args[0]).enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        jda.updateCommands().addCommands(
                Commands.slash("起動1", "ランダムに文章と画像を表示します。"),
                Commands.slash("add", "メッセージと画像を追加します。")
                        .addOption(OptionType.STRING, "メッセージ", "送信するメッセージ内容", true)
                        .addOption(OptionType.STRING, "画像url", "送信する画像へのリンク", true)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Commands.slash("delete", "メッセージを全削除します。")
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        ).queue();
        jda.addEventListener(new Main());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equals("起動1"))
        {
            SecureRandom sc = new SecureRandom();
            if(node.size() == 0)
            {
                e.reply(":no_entry_sign: 未登録です。").setEphemeral(true).queue();
                return;
            }
            int a = sc.nextInt(node.size());
            String[] res = node.get(""+a).asText().split("\\|");
            e.reply(res[0]).queue();
            e.getChannel().sendMessage(res[1]).queue();
            /*
            String[] list = {"文章1"};
            try
            {
                URL url = new URL("https://cdn.discordapp.com/attachments/1064793463306072175/1064793483132547102/IMG_1536.png");
                File tmp = File.createTempFile("imaage", null);
                URLConnection conn = url.openConnection();
                InputStream in = conn.getInputStream();
                FileOutputStream out = new FileOutputStream(tmp, false);
                int b;
                while((b = in.read()) != -1){
                    out.write(b);
                }
                out.close();
                in.close();
                e.getHook().sendFiles(FileUpload.fromData(tmp, "imaage.png")).queue();
                e.getChannel().sendMessage(list[sc.nextInt(1)]).queue();
            }
            catch(IOException ex)
            {
                throw new RuntimeException(ex);
            }
             */
        }
        if(e.getName().equals("add"))
        {
            node.put(String.valueOf(node.size()+1), "%s|%s".formatted(e.getOption("メッセージ").getAsString(), e.getOption("画像url").getAsString()));
            reload();
            e.reply("✅ 登録しました。").setEphemeral(true).queue();
        }
        if(e.getName().equals("delete"))
        {
            node.removeAll();
            reload();
            e.reply("✅ 全削除しました。").setEphemeral(true).queue();
        }
    }
    public static void reload()
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
            bw.write(new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(node));
            bw.flush();
            bw.close();
            node = new ObjectMapper().readTree(FILE).deepCopy();
        }
        catch (IOException ignored) {}
    }
}