import Head from 'next/head'

function Error({ statusCode }) {
  return (
    <>
      <Head>
        <title>Level Uphaken</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
            <div className="row">
                <div className="col mb-3">
                    <h3>Da ist etwas schiefgelaufen...</h3>
                </div>
            </div>
        </div>
        <section className="section">
            <div className="card">
                <div className="card-body">
                {statusCode
                  ? `An error ${statusCode} occurred on server`
                  : 'An error occurred on client'}
                </div>
            </div>
        </section>
      </div>
    </>
  )
}

Error.getInitialProps = ({ res, err }) => {
  const statusCode = res ? res.statusCode : err ? err.statusCode : 404
  return { statusCode }
}

export default Error
