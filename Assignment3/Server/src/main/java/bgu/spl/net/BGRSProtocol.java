package bgu.spl.net;

import bgu.spl.net.api.MessagingProtocol;



public class BGRSProtocol implements MessagingProtocol<Message> {

    private User user = null;
    private Database data = Database.getInstance() ;
    private boolean shouldTerminate = false;

    @Override
    public Message process(Message msg) {
        String[] details;
        short courseNum;
        switch (msg.getOpcode()){
            //1) Admin register
            case ((short)1):
                //checking if input is legal
                try{
                    details = processMsg(msg.getMessage(),2);
                }catch (Exception e){
                    return msgFailed((short)1);
                }
                if(user != null || data.isUserRegistered(details[0])){
                    return msgFailed((short)1);
                }
                else{
                    data.RegisterUser(details[0],details[1],true);
                    return msgSuccess((short)1);
                }
                /**
                 2) student register
                 */
            case ((short)2):
                //checking if input is legal
                try{
                    details = processMsg(msg.getMessage(),2);
                }catch (Exception e){
                    return msgFailed((short)2);
                }
                if(user != null || data.isUserRegistered(details[0])){
                    return msgFailed((short)2);
                }
                else{
                    data.RegisterUser(details[0],details[1],false);
                    return msgSuccess((short)2);
                }
                /**
                 * 3) login request
                 */
            case ((short)3):
                //checking if input is legal
                try{
                    details = processMsg(msg.getMessage(),2);
                }catch (Exception e){
                    return msgFailed((short)3);
                }
                if(!data.isUserRegistered(details[0]) || user != null || data.isLogin(details[0]) || !data.getUserPassword(details[0]).equals(details[1])) {
                    return msgFailed((short) 3);
                }
                else{
                    data.setLogin(details[0],true);
                    user = data.getUser(details[0]);
                    return msgSuccess((short)3);
                }
                /**
                 * 4)logout request
                 */
            case ((short)4):
                if(user == null || !user.isLogin())
                    return msgFailed((short)4);
                else{
                    data.setLogin(user.getUserName(),false);
                    user = null;
                    return msgSuccess((short)4);
                }

                /**
                 *  5) register to course
                 */
            case ((short)5):
                courseNum = msg.getCourseNum();
                if(user == null || user.isAdmin() || user.getStudentCourses().contains(courseNum) || !data.courseExists(courseNum) || !data.userHasAllKdamCourses(user.getUserName(),courseNum) || !data.isCourseAvailable(courseNum)) {/*add has all kdam and couse exists*/
                    return msgFailed((short) 5);
                }
                else{
                    data.regStudentToCourse(user.getUserName(),courseNum);
                    return msgSuccess((short)5);
                }
                /**
                 * 6) check kdam course
                 */
            case ((short)6):
                courseNum = msg.getCourseNum();
                if(user == null || !data.courseExists(courseNum)){
                    return msgFailed((short)6);
                }
                else{
                    String kdamCourses = data.getKdamCourses(courseNum).toString().replaceAll(" ","");
                    return msgSuccessAndReply((short)6,kdamCourses);
                }
                /**
                 * 7) print course status (admin only)
                 */
            case ((short)7):
                courseNum = msg.getCourseNum();
                if(user == null || !user.isAdmin() || !data.courseExists(courseNum)){

                    return msgFailed((short)7);
                }
                else{
                    return msgSuccessAndReply((short)7,data.getCourse(courseNum).toString());
                }
                /**
                 * 8) print student status (admin only)
                 */
            case ((short)8):
                //checking if input is legal
                try{
                    details = processMsg(msg.getMessage(),1);
                }catch (Exception e){
                    return msgFailed((short)8);
                }
                if(user == null || !user.isAdmin() || !data.isUserRegistered(details[0]) || data.isAdmin(details[0])){
                    return msgFailed((short)8);
                }
                else{
                    return msgSuccessAndReply((short)8,data.getUser(details[0]).toString());
                }
                /**
                 * 9) isRegistered
                 */
            case ((short)9):
                courseNum = msg.getCourseNum();
                if(user == null || user.isAdmin() || !data.courseExists(courseNum)){
                    return msgFailed((short)9);
                }
                else if(!data.isStudentRegisterToCourse(user.getUserName(),courseNum)){
                    return msgSuccessAndReply((short)9,"NOT REGISTERED");
                }
                else{
                    return msgSuccessAndReply((short)9,"REGISTERED");
                }
                /**
                 * 10) unregister student for some course
                 */
            case ((short)10):
                courseNum = msg.getCourseNum();
                if(user == null || user.isAdmin() || !data.courseExists(courseNum) || !data.isStudentRegisterToCourse(user.getUserName(),courseNum)){
                    return msgFailed((short)10);
                }
                else{
                    data.unregisterStudentToCourse(user.getUserName(),courseNum);
                    return msgSuccess((short)10);
                }
                /**
                 * 11) my courses
                 */
            case ((short)11):
                if(user == null || user.isAdmin()){
                    return msgFailed((short)11);
                }
                else{
                    return msgSuccessAndReply((short)11,user.getStudentCourses().toString().replaceAll(" ",""));
                }
        }
        return msgFailed((short)13);
    }

    private Message msgSuccessAndReply(short ackNum,String addMessage){
        return new Message((short)12,ackNum,"\n"+addMessage);
    }
    private Message msgSuccess(short ackNum){
        return new Message((short)12,ackNum,"");
    }
    private Message msgFailed(short errNum){
        return new Message((short)13,errNum);
    }
    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    private String[] processMsg(String s,int _case){
        if(!isLegal(s,_case))
            throw new IllegalArgumentException();
        String [] output = new String[_case];
        //in case that there is only one parameter in the message for example in courseStat question
        if(_case == 1){
            output[0] = s;
        }
        //in case that there are 2 parameters for example username and password
        if(_case == 2){
            int spaceIndex = s.indexOf('\0');
            output[0] = s.substring(0,spaceIndex);
            output[1] = s.substring(spaceIndex+1);
        }
        //in case that there is no user name or password or course num ect
        return output;

    }
    private boolean isLegal(String s, int _case){
        if(_case == 1){
            if(s.indexOf('\0') >= 0)
                return false;
        }
        if(_case == 2) {
            if (s.indexOf('\0') <= 0) {
                return false;
            }
        }
        return true;
    }
}
