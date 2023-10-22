import Head from 'next/head'
import { signOut } from 'next-auth/client'
import { useEffect } from 'react'

export default function LogOut() {

  useEffect(() => signOut({ callbackUrl: '/auth/login?error=Sie wurden abgemeldet!' }), [])

  return (
    <>
      <Head>
        <title>Abmeldung</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
          <h1 className="auth-title mb-4">Abmeldung</h1>
        </div>
        <section className="section">
          Sie werden weitergeleitet
        </section>
      </div>
    </>
  )
}
