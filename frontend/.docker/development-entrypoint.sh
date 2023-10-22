grep -s API_ENDPOINT .env.local > /dev/null || echo 'API_ENDPOINT=http://backend:8080' >> .env.local
grep -s PUBLIC_API_ENDPOINT .env.local > /dev/null || echo 'PUBLIC_API_ENDPOINT=http://localhost:8080' >> .env.local
grep -s NEXTAUTH_URL .env.local > /dev/null || echo 'NEXTAUTH_URL=http://localhost:3000' >> .env.local
grep -s SECRET .env.local > /dev/null || date | md5sum | cut -d' ' -f1 | sed -e 's/^/SECRET=/;' >> .env.local
grep -s JWT_SECRET .env.local > /dev/null || date | md5sum | cut -d' ' -f1 | sed -e 's/^/JWT_SECRET=/;' >> .env.local
grep -s JWT_SIGNING_KEY .env.local > /dev/null || npx -q node-jose-tools newkey -s 256 -t oct -a HS512 | sed -e 's/^/JWT_SIGNING_KEY=/;' | sed -e 's/$/\n/;' >> .env.local 2> /dev/null
grep -s JWT_ENCRYPTION_KEY .env.local > /dev/null || npx -q node-jose-tools newkey -s 256 -t oct -a A256GCM -u enc | sed -e 's/^/JWT_ENCRYPTION_KEY=/;' | sed -e 's/$/\n/;' >> .env.local 2> /dev/null
npm install
mkdir -p public/vendor/fonts/
cp -f node_modules/bootstrap/dist/js/bootstrap.bundle.js* public/vendor/
cp -f node_modules/perfect-scrollbar/dist/perfect-scrollbar.js* public/vendor/
npm run-script dev