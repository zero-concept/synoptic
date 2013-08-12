package algorithms;

import java.io.IOException;
import java.util.logging.Logger;

import model.InvModel;
import model.InvsModel;

import synoptic.invariants.ITemporalInvariant;
import synoptic.invariants.TemporalInvariantSet;

public class InvComposition {

    public static Logger logger;
    static {
        logger = Logger.getLogger("InvComposition");
    }

    /**
     * Constructs an InvsModel by intersecting InvModels for each of the given
     * temporal invariants.
     * 
     * @param invariants
     *            a set of TemporalInvariants
     * @param minimize
     *            whether or not to minimize the model before returning.
     * @return the intersected InvsModel
     * @throws IOException
     */
    public static InvsModel intersectModelWithInvs(
            TemporalInvariantSet invariants, boolean minimizeDFAIntersections,
            InvsModel model) throws IOException {

        // Intersect invariants into model.
        int total = invariants.numInvariants();
        int numDone = 0;

        for (ITemporalInvariant invariant : invariants) {
            // logger.info("Create new invDFA instance, remaining" + remaining);
            InvModel invDFA = new InvModel(invariant, model.getEventEncodings());
            model.intersectWith(invDFA);
            invDFA = null;

            // Optimize by minimizing the model every 100 intersections.
            if ((numDone % 20 == 0) && minimizeDFAIntersections) {
                logger.info("Intersected " + numDone + " / " + total
                        + " invariants.");
                model.minimize();
            }

            numDone += 1;
        }

        if (minimizeDFAIntersections) {
            model.minimize();
        }

        return model;
    }
}