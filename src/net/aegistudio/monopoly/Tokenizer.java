package net.aegistudio.monopoly;

public abstract class Tokenizer {
	public StringBuilder builder = new StringBuilder();
	public StringBuilder tokenBuilder = new StringBuilder();
	
	public Tokenizer(Configuration config, String tokenize) {
		boolean outsideToken = true;
		while(tokenize.length() > 0) {
			if(outsideToken) {
				if(tokenize.startsWith(config.BLOCK_BEGIN_TOKEN)) {
					outsideToken = false;
					tokenize = tokenize.substring(config.BLOCK_BEGIN_TOKEN.length());
				}
				else {
					builder.append(tokenize.charAt(0));
					tokenize = tokenize.substring(1);
				}
			}
			else {
				if(tokenize.startsWith(config.BLOCK_END_TOKEN)) {
					outsideToken = true;
					this.builder.append(this.acceptToken(new String(tokenBuilder)));
					this.tokenBuilder = new StringBuilder();
					tokenize = tokenize.substring(config.BLOCK_END_TOKEN.length());
				}
				else {
					tokenBuilder.append(tokenize.charAt(0));
					tokenize = tokenize.substring(1);
				}
			}
		}
	}
	
	public String getResult() {
		return new String(builder);
	}
	
	public abstract String acceptToken(String token);
}
