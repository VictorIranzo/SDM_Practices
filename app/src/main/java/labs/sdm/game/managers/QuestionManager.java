package labs.sdm.game.managers;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.widget.Switch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import labs.sdm.game.R;
import labs.sdm.game.pojo.Question;

public class QuestionManager {

    public static List<Question> GetQuestions(Context context)
    {
        XmlResourceParser parser = context.getResources().getXml(R.xml.questions);
        List<Question> questions = new ArrayList<Question>();

        try
        {
            int eventType = parser.getEventType();
            while(eventType != XmlResourceParser.END_DOCUMENT)
            {
                // In the comparison is better compare first the constant value (see joda rule) for
                // prevention of null values.
                if(!"quizz".equals(parser.getName()) && eventType == XmlResourceParser.START_TAG){
                    Question question = new Question(
                        parser.getAttributeValue(null,"number"),
                        parser.getAttributeValue(null,"text"),
                        parser.getAttributeValue(null,"answer1"),
                        parser.getAttributeValue(null,"answer2"),
                        parser.getAttributeValue(null,"answer3"),
                        parser.getAttributeValue(null,"answer4"),
                        parser.getAttributeValue(null,"right"),
                        parser.getAttributeValue(null,"audience"),
                        parser.getAttributeValue(null,"phone"),
                        parser.getAttributeValue(null,"fifty1"),
                        parser.getAttributeValue(null,"fifty2")
                    );
                    questions.add(question);
                }
                parser.next();
                eventType = parser.getEventType();
            }
        }
        catch (XmlPullParserException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

        return questions;
    }
}
