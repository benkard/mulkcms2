# SPDX-FileCopyrightText: © 2022 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
#
# SPDX-License-Identifier: GPL-3.0-or-later

FROM registry.access.redhat.com/ubi8/ubi-minimal:latest AS ubi-minimal

# Add OpenJDK (complete).
ADD jdk-dist/latest.tar.gz /jdk

# JLink a trimmed JDK.
RUN microdnf --assumeyes --nodocs install binutils
RUN /jdk/*/bin/jlink \
    -J-XX:MaxRAMPercentage=75 \
    --add-modules ALL-MODULE-PATH \
    --compress=1 \
    --no-man-pages \
    --strip-debug \
    --output /java
RUN /java/bin/java -Xshare:dump

# Set mtimes to @0 for reproducibility.
RUN microdnf --assumeyes --nodocs install findutils
RUN find /java -exec touch --date=@0 '{}' ';'

# Build the final image.
FROM registry.access.redhat.com/ubi8/ubi-micro:latest
COPY --from=ubi-minimal /lib64/libz.so.1 /lib64/
COPY --from=ubi-minimal /java /java
ENV JAVA_HOME=/java
ENV PATH=/java/bin:$PATH
