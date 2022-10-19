package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"net"
	"net/http"
	"os"
	"os/signal"
	"syscall"

	"github.com/go-chi/chi"
	"golang.org/x/sync/errgroup"
)

func main() {

	mux := chi.NewRouter()

	// 確認用。
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json; charset=utf-8")
		fmt.Println("health !")
		_, _ = w.Write([]byte(`{"status": "ok"}`))
	})

	mux.Post("/register", register)

	// 適当に 8080 で待つか。
	l, err := net.Listen("tcp", ":8080")
	if err != nil {
		log.Fatalf("Failed to listen port 8080: %v", err)
	}

	Run(context.Background(), mux, l)
}

// FCM を受け取って登録する関数。
func register(w http.ResponseWriter, r *http.Request) {

	ctx := r.Context()

	var b struct {
		Token string `json:"token"`
	}

	if err := json.NewDecoder(r.Body).Decode(&b); err != nil {
		RespondJSON(ctx, w, err.Error(), http.StatusInternalServerError)
		return
	}
	fmt.Println(b)

	RespondJSON(ctx, w, "rsp", http.StatusOK)
}

func RespondJSON(ctx context.Context, w http.ResponseWriter, body any, status int) {
	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	bodyBytes, err := json.Marshal(body)

	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	w.WriteHeader(status)
	if _, err := fmt.Fprintf(w, "%s", bodyBytes); err != nil {
		fmt.Printf("write response error: %v", err)
	}
}

func Run(ctx context.Context, mux http.Handler, l net.Listener) error {

	ctx, stop := signal.NotifyContext(ctx, os.Interrupt, syscall.SIGTERM)
	defer stop()

	server := &http.Server{Handler: mux}

	eg, ctx := errgroup.WithContext(ctx)
	eg.Go(func() error {
		if err := server.Serve(l); err != nil && err != http.ErrServerClosed {
			log.Printf("failed to close: %+v", err)
			return err
		}
		return nil
	})

	<-ctx.Done()
	if err := server.Shutdown(context.Background()); err != nil {
		log.Printf("Faailed to shutdown: %+v", err)
	}
	return eg.Wait()
}
