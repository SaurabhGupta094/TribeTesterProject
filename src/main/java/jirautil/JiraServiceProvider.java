package jirautil;

import java.io.File;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.Issue.FluentCreate;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;

public class JiraServiceProvider {

	private JiraClient jira;
	private String project;

	public JiraServiceProvider(String jiraUrl, String username, String password, String project) {
		// create basic authentication object
		BasicCredentials creds = new BasicCredentials(username, password);
		// initialize the jira client with the url and the credentials
		jira = new JiraClient(jiraUrl, creds);
		this.project = project;
	}

	public Issue createJiraIssue(String issueType, String summary, String description, String sScreenshotFilePath) {

		/* Create a new issue. */
		Issue newIssue = null;
		try {
			FluentCreate newIssueFluentCreate = jira.createIssue(project, issueType);
			// Add the summary
			newIssueFluentCreate.field(Field.SUMMARY, summary);
			// Add the description
			newIssueFluentCreate.field(Field.DESCRIPTION, description);


			// create the issue in the jira server
			newIssue = newIssueFluentCreate.execute();
			System.out.println("New issue created. Jira ID : " + newIssue);
			if (sScreenshotFilePath != null)
				newIssue.addAttachment(new File(sScreenshotFilePath));
			System.out.println(sScreenshotFilePath);
			
		} catch (JiraException e) {
			System.out.println("Exception occured while raising defect : " + e.getMessage());

		}
		return newIssue;
	}

}
