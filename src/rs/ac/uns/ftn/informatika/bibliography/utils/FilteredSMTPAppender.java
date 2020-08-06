package rs.ac.uns.ftn.informatika.bibliography.utils;

import org.apache.log4j.net.SMTPAppender;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class FilteredSMTPAppender extends SMTPAppender {
  private int timeFrame;
  private int maxEMails;
  protected long timeFrameMillis;
  protected List<Date> exceptionDates = new ArrayList<Date>();
  public int getTimeFrame() {
    return timeFrame;
  }
  public void setTimeFrame(int timeFrame) {
    this.timeFrame = timeFrame;
  }
  public int getMaxEMails() {
    return maxEMails;
  }
  public void setMaxEMails(int maxEMails) {
    this.maxEMails = maxEMails;
  }
  @Override
  public void activateOptions()
  {
    super.activateOptions();
    timeFrameMillis = timeFrame * 60 * 1000;
  }
  protected void cleanTimedoutExceptions()
  {
    Date current = new Date();
    // Remove timedout exceptions
    Iterator<Date> itr = exceptionDates.iterator();
    while (itr.hasNext())
    {
      Date exceptionDate = itr.next();
      if (current.getTime() - exceptionDate.getTime() > timeFrameMillis)
      {
        itr.remove();
      }
      else
      {
        break;
      }
    }
  }
  protected void addException()
  {
    exceptionDates.add(new Date());
  }
  protected boolean isSendMailAllowed()
  {
    return exceptionDates.size() < maxEMails;
  }
  
  @Override
  protected void sendBuffer()
  {
    cleanTimedoutExceptions();
    if (isSendMailAllowed())
    {
      super.sendBuffer();
      addException();
    }
  }
}