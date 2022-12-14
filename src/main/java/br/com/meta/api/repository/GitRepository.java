package br.com.meta.api.repository;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.jboss.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import br.com.meta.api.exception.GitCloneException;
import br.com.meta.api.exception.RepositorioNaoExisteException;
import br.com.meta.api.model.Autor;

@Repository
public class GitRepository {
	
	protected final Logger LOGGER = Logger.getLogger(GitRepository.class);
	private final String BASE_GITHUB_API = "https://api.github.com/repos/%s/%s";
	
	public void clonar(String repositorioURL, File diretorio) {
		
		try {
			CloneCommand command = new CloneCommand();
			command.setURI(repositorioURL);
			command.setDirectory(diretorio);
			command.call();
		} catch (GitAPIException e) {
			throw new GitCloneException();
		} catch (JGitInternalException e) {
			// Não deve fazer nenhum tratamento para essa exception.
		} catch (Exception e) {
			throw new RepositorioNaoExisteException();
		}
	}
	
	public String buscarRepositorio(String usuario, String repositorio) {
		try {
			RestTemplate rt = new RestTemplate();
			ResponseEntity<Autor> response = rt.getForEntity(String.format(BASE_GITHUB_API, usuario, repositorio), Autor.class);
			return response.getBody().getHtml_url().concat(".git");
		} catch (Exception e) {
			throw new RepositorioNaoExisteException();
		}
	}
}
