package ch.syseca.sstl.util.misc;


import org.slf4j.Logger;


/**
 * Enumeration, der die Zust&auml;nde
 *
 * <ul>
 * <li>TRACE</li>
 * <li>INFO</li>
 * <li>WARN</li>
 * <li>ERROR</li>
 * <li>FATAL</li>
 * </ul>
 */
public enum Severity {

    TRACE {
        @Override
        public void log(Logger logger, Object msg, Throwable e) {
            logger.trace(msg.toString(), e);
        }


        @Override
        public void log(Logger logger, Object msg) {
            logger.trace(msg.toString());
        }
    },

    INFO {
        @Override
        public void log(Logger logger, Object msg, Throwable e) {
            logger.info(msg.toString(), e);
        }


        @Override
        public void log(Logger logger, Object msg) {
            logger.info(msg.toString());
        }
    },

    WARN {
        @Override
        public void log(Logger logger, Object msg, Throwable e) {
            logger.warn(msg.toString(), e);
        }


        @Override
        public void log(Logger logger, Object msg) {
            logger.warn(msg.toString());
        }
    },

    ERROR {
        @Override
        public void log(Logger logger, Object msg, Throwable e) {
            logger.error(msg.toString(), e);
        }


        @Override
        public void log(Logger logger, Object msg) {
            logger.error(msg.toString());
        }
    },

    FATAL {
        @Override
        public void log(Logger logger, Object msg, Throwable e) {
            logger.error(msg.toString(), e);
        }


        @Override
        public void log(Logger logger, Object msg) {
            logger.error(msg.toString());
        }
    };

    /**
     * Gibt <code>true</code> zur&uuml;ck, wenn der Zustand einen schlimmeren Zustand als <code>other</code> darstellt. Dies
     * ist der Fall, wenn
     *
     * <pre>
     * this.ordinal() &gt; other.ordinal()
     * </pre>
     *
     * zutrifft.
     *
     * @param   other  Der andere Zustand.
     *
     * @return  <code>true</code>, wenn der Zustand einen schlimmeren Zustand als <code>other</code> darstellt.
     */
    public boolean worseThan(Severity other) {
        return this.ordinal() > other.ordinal();
    }


    public static Severity worstOf(Severity first, Severity second) {
        return second.worseThan(first) ? second : first;
    }


    public static Severity worstOf(Severity first, Severity second, Severity... others) {
        Severity result = worstOf(first, second);

        for (Severity s : others) {
            if (s.worseThan(result)) {
                result = s;
            }
        }

        return result;
    }


    /**
     * Logged die Meldung auf dem entsprechenden Log-Level.
     *
     * @param  logger  Logger.
     * @param  msg     Siehe {@link Logger#info(String)}
     */
    public abstract void log(Logger logger, Object msg);


    /**
     * Logged die Meldung auf dem entsprechenden Log-Level.
     *
     * @param  logger  Logger.
     * @param  msg     Siehe {@link Logger#info(String)}
     * @param  e       Siehe {@link Logger#info(String, Throwable)}
     */
    public abstract void log(Logger logger, Object msg, Throwable e);
}
