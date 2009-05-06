WiseIO is meant to be a *small* and *simple* library, to perform IO operations and handle IOExceptions properly.

You've always been bothered by IOExceptions and how you really should handle them when closing and flushing your streams, but were tired of the boilerplate? You've always thought IOUtils.closeSilently() in a finally block was a huge mistake but still the best you could afford ? This is for you.


        
final Properties props = new Properties();

new ClosingIO<InputStream>(in, new IOOperation() {
    public void op() throws IOException {
        props.load(in);
    }
 }).exec();

