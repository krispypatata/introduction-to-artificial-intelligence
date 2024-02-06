package bow;

import java.math.BigDecimal;

public class OutputData {
	private String fileName;
	private String classOfMail;
	private BigDecimal p_spam;

	// constructor
	public OutputData(String fileName, String classOfMail, BigDecimal p_spam) {
		this.setFileName(fileName);
		this.setClassOfMail(classOfMail);
		this.setP_spam(p_spam);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getClassOfMail() {
		return classOfMail;
	}

	public void setClassOfMail(String classOfMail) {
		this.classOfMail = classOfMail;
	}

	public BigDecimal getP_spam() {
		return p_spam;
	}

	public void setP_spam(BigDecimal p_spam) {
		this.p_spam = p_spam;
	}
}
